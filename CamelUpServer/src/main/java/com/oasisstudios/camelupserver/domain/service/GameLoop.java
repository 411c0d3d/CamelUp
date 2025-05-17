package com.oasisstudios.camelupserver.domain.service;

import com.google.gson.Gson;
import com.oasisstudios.camelupserver.dataaccess.IPlayerMove;
import com.oasisstudios.camelupserver.dataaccess.dto.*;
import com.oasisstudios.camelupserver.dataaccess.jsonstorage.LobbyService;
import com.oasisstudios.camelupserver.domain.model.domainclasses.*;
import com.oasisstudios.camelupserver.domain.model.handlers_managers.*;
import com.oasisstudios.camelupserver.domain.model.mappers.GameStateMapper;
import com.oasisstudios.camelupserver.domain.model.turn.TurnManager;
import com.oasisstudios.camelupserver.domain.repository.ClientHandlersRepository;
import com.oasisstudios.camelupserver.server.IPacketListener;
import com.oasisstudios.camelupserver.server.ParticipantHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

import static com.oasisstudios.camelupserver.dataaccess.dto.Packet.Type.GAME_END;
import static com.oasisstudios.camelupserver.dataaccess.dto.Packet.Type.GAME_STATE;

/**
 * The type Game loop.
 */
public class GameLoop implements Runnable {
    private final Lobby lobby;
    private final ClientHandlersRepository clientHandlersRepository;
    /**
     * The Participant handlers.
     */
    private final CopyOnWriteArraySet<ParticipantHandler> participantHandlers;
    private PlayerDOM currentPlayer;
    private final ExecutorService executorService;
    /**
     * The Is running.
     */
    public boolean IsRunning;
    private Lock gameStateLock;  // Lock to handle concurrency on game state
    private Gson gson;
    private ActionValidationService actionValidationService;
    private DicePyramidDOM dicePyramidDOM;
    private PlayerRollHandler playerRollHandler;
    private FinalBetHandler finalBetHandler;
    private PlayerBetHandler playerBetHandler;
    private PlayerCardHandler playerCardHandler;
    private PlayerPenaltyHelper playerPenaltyHelper;
    private GameStateDOM gameStateDOM;
    private BoardSpacesCourseHelper boardSpacesCourseHelper;
    private CamelMovementHelper camelMovementHelper;
    private StageRatingService stageRatingService;
    private TurnManager turnManager;
    private StageResetService stageResetService;
    private PlayerHandler playerHandler;
    private TurnRewardHelper turnRewardHelper;
    private EndGameRatingService endGameRatingService;
    private BoardSpacesCourseDOM boardSpacesCourseDOM;

    /**
     * Initialize.
     */
    public void initialize() {
        this.gameStateDOM = GameStateMapper.initializeGameStateDOM(this.lobby.getGameState());
        this.gson = new Gson();
        this.dicePyramidDOM = new DicePyramidDOM(gameStateDOM);
        this.boardSpacesCourseDOM = gameStateDOM.getBoardSpacesCourse();
        this.boardSpacesCourseHelper = new BoardSpacesCourseHelper(gameStateDOM.getBoardSpacesCourse());
        this.playerPenaltyHelper = new PlayerPenaltyHelper(gameStateDOM, boardSpacesCourseHelper);
        this.camelMovementHelper = new CamelMovementHelper();
        this.playerRollHandler = new PlayerRollHandler(gameStateDOM, boardSpacesCourseHelper, camelMovementHelper, playerHandler);
        this.playerHandler = new PlayerHandler(gameStateDOM, playerPenaltyHelper, new TurnRewardHelper());
        this.stageRatingService = new StageRatingService(gameStateDOM, boardSpacesCourseHelper);
        this.playerCardHandler = new PlayerCardHandler(boardSpacesCourseHelper);
        this.playerBetHandler = new PlayerBetHandler(gameStateDOM);
        this.finalBetHandler = new FinalBetHandler(gameStateDOM);
        this.endGameRatingService = new EndGameRatingService(boardSpacesCourseHelper, gameStateDOM);
        this.actionValidationService = new ActionValidationService(playerHandler,
                finalBetHandler,
                playerBetHandler,
                playerCardHandler);

        turnManager = new TurnManager(
                gameStateDOM,
                actionValidationService,
                playerRollHandler,
                playerCardHandler,
                finalBetHandler,
                playerBetHandler,
                playerHandler,
                dicePyramidDOM); // maybe is not needed because implied wi
        initializeBoardSpaceCourseWithCamels();
    }

    /**
     * Instantiates a new Game loop.
     *
     * @param lobby the lobby 
     * @param playerClientHandlers the participant handlers
     */
    public GameLoop(Lobby lobby, CopyOnWriteArraySet<ParticipantHandler> playerClientHandlers) {
        this.lobby = lobby;
        this.participantHandlers = playerClientHandlers;
        this.executorService = Executors.newSingleThreadExecutor();
        clientHandlersRepository = ClientHandlersRepository.getClientHandlersRepository();
        initialize();
    }

    /**
     * Initialize board space course with camels.
     */
    public void initializeBoardSpaceCourseWithCamels() {
        this.dicePyramidDOM.initialLineupRefill();
        while (!this.dicePyramidDOM.isEmpty()) {
            RolledDiceDOM rolledDice = dicePyramidDOM.roll();
            CamelDOM camel = rolledDice.getCamel();
            // Step 8: Handle the camel lineup roll
            this.playerRollHandler.handleCamelLineupRoll(rolledDice);
            // sets the initial pyramid stageRefill for the upcoming start of the gameloop
            dicePyramidDOM.regularStageRefill();
        }
    }
    // Game-Ending relevant
    // isFinishLineReached() --> call EndGameRatingService + termination of game (not implemented?)
    // hasGameSufficientPlayingPlayers --> stop the game

    // Stage Ending relevant
    // if !pyramid.isEmpty() --> if empty == true: stage has been won --> stageratingService --> stageresetservice
    // if !pyramid.isEmpty() --> if empty == false --> it is allowed to call the turn manager because pyramid still has dice
    @Override
    public void run() {
        IsRunning = true;

        long startTime = System.currentTimeMillis();
        long elapsedTime = System.currentTimeMillis() - startTime; // Time since start in milliseconds
        while (IsRunning) {
            updateParticipantsRepository();
            // Check whether the Lobby contains Sufficient Playing Players and if no camel is at the finish line already
            // this.boardSpacesCourseHelper.isFinishLineReached() --> True --> call endgamerating --> finish game --> broadcast "finished" gamestate
            // this.playerHandler.hasGameSufficientPlayingPlayers() --> Speilaabruch, admin entscheidet über gewinner --> broadcast "finished" gamestate
            // this.gameStateDOM.isGameWithinTurnsLimit() --> Speilaabruch, admin entscheidet über gewinner --> broadcast "finished" gamestate

            // hasGameSufficientPlayingPlayers breaks the loop --> no end game rating --> adding picks winners / / or randomized --> how to close the while?
            // elapsedTime has to be checked ALL the time --> outer thread -> LobbyManager
            // we have to write the elapsed time into gamestate before boradcasting --> done

            currentPlayer = playerHandler.determineCurrentPlayerForTurn();
            if (currentPlayer == null) {
                visualizing(GameStateDOM.GamePhase.FINISHED);
                gameStateDOM.setGameDuration((int) elapsedTime);
                updateAndBroadcastGameState();
                break;
            }

            if (dicePyramidDOM.isEmpty()) {
                stageRatingService.performStageRatingForAllPlayers();
                stageResetService.resetStage();
                dicePyramidDOM.regularStageRefill();
                elapsedTime = System.currentTimeMillis() - startTime;
                gameStateDOM.setGameDuration((int) elapsedTime);
                visualizing(GameStateDOM.GamePhase.VISUALIZING);
                updateAndBroadcastGameState();
                continue;
            }

            // Turn Starts, awaits players move, set gamePhase to "playing"
            gameStateDOM.setGamePhase(GameStateDOM.GamePhase.PLAYING);
            gameStateDOM.setGameDuration((int) elapsedTime);
            updateAndBroadcastGameState();
            boolean isMoveMade = resolvePlayerMove(currentPlayer).join();
            if (!isMoveMade) {
                playerHandler.penalizeIllegalMove(currentPlayer.getPlayerId());
            }

            // Check if the 3 game-end criteria have been reached. If yes, perform end game rating set "finished" and don't set "visualize" and break the loop.
            if (this.boardSpacesCourseHelper.isFinishLineReached() || !this.gameStateDOM.isGameWithinTurnsLimit() || !this.gameStateDOM.isGameWithinMaxGameDuration()) {
                this.endGameRatingService.performEndGameRatingForAllPlayers(); // only difference between breaking and ending
                visualizing(GameStateDOM.GamePhase.FINISHED);
                gameStateDOM.setGameDuration((int) elapsedTime);
                updateAndBroadcastGameState();
                break;
            }

            visualizing(GameStateDOM.GamePhase.VISUALIZING);
            gameStateDOM.setGameDuration((int) elapsedTime);
            updateAndBroadcastGameState();
            playerHandler.endCurrentPlayersTurn(currentPlayer.getPlayerId());

//            // Turn is done, end turn, updateGameState, set gamePhase to "visualizing"
//            playerHandler.endCurrentPlayersTurn(currentPlayer.getPlayerId());
//            visualizing(GameStateDOM.GamePhase.VISUALIZING);
//            gameStateDOM.setGameDuration((int)elapsedTime);
//            updateGameState();

            IsRunning = this.playerHandler.hasGameSufficientPlayingPlayers(); // break: end game --> call a method that does "finished", update, broadcast but no endgamerating
        }

        if (!this.endGameRatingService.isRated) {
            visualizing(GameStateDOM.GamePhase.FINISHED);
            gameStateDOM.setGameDuration((int) elapsedTime);
            updateAndBroadcastGameState();
        }
        updateAndBroadcastFinishedGame();
    }


//    private CompletableFuture<Boolean> awaitPlayerMove(PlayerDOM currentPlayer) {
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                long thinkingTime = this.gameStateDOM.getGameConfigDOM().getThinkingTime();
//                long startTime = System.currentTimeMillis();
//                IPlayerMove playerMove = new RollDice(); // TODO: Replace with event return value
//                boolean isMoveMade = turnManager.action(playerMove, currentPlayer.getPlayerId());
//                long endTime = System.currentTimeMillis();
//                long timeSpent = endTime - startTime;
//                Thread.sleep(timeSpent);
//                return isMoveMade && timeSpent <= thinkingTime;
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                return false;
//            }
//        });
//    }

    private CompletableFuture<Boolean> resolvePlayerMove(PlayerDOM currentPlayer) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                long thinkingTime = this.gameStateDOM.getGameConfigDOM().getThinkingTime();
                long startTime = System.currentTimeMillis();
                ParticipantHandler currentPlayerClientHandler = null;
                for (ParticipantHandler playerClientHandler : participantHandlers) {
                    if (playerClientHandler.getClientAck().getClientId() == currentPlayer.getPlayerId()) {
                        currentPlayerClientHandler = playerClientHandler;
                        break;
                    }
                }
                if (currentPlayerClientHandler == null) {
                    return false;
                }
                // Await player move from server
                IPlayerMove playerMove = waitForPlayerMove(currentPlayerClientHandler);
                long endTime = System.currentTimeMillis();
                long timeSpent = endTime - startTime;
                boolean isMoveMade = turnManager.action(playerMove, currentPlayer.getPlayerId());
                // Simulate thinking delay if needed
                Thread.sleep(Math.max(0, thinkingTime - timeSpent));
                return isMoveMade && timeSpent <= thinkingTime;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        });
    }

    private IPlayerMove waitForPlayerMove(ParticipantHandler playerClientHandler) {
        final Object lock = new Object();
        final IPlayerMove[] playerMoveHolder = new IPlayerMove[1];

        // Define the listener to capture the player's move
        IPacketListener moveListener = packet -> {
            if (packet instanceof IPlayerMove) {
                synchronized (lock) {
                    playerMoveHolder[0] = (IPlayerMove) packet;
                    lock.notifyAll(); // Notify the waiting thread
                }
            }
        };

        // Set the listener in the ClientHandler
        playerClientHandler.setMoveListener(moveListener);

        synchronized (lock) {
            while (playerMoveHolder[0] == null) {
                try {
                    lock.wait(); // Wait until a move is received
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        // Clear the listener after use
        playerClientHandler.clearMoveListener();
        return playerMoveHolder[0]; // Return the received move
    }

    /**
     * Visualizing.
     *
     * @param gamePhase the game phase
     */
    public void visualizing(GameStateDOM.GamePhase gamePhase) {
        gameStateDOM.setGamePhase(gamePhase);
        try {
            Thread.sleep(gameStateDOM.getGameConfigDOM().getVisualizationTime());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // this is the general structure of any method that mutates the Game State anywhere from this point on
    // once the state is mutated you have to call the Lobby Persistence to save the changes to json file by lobbyId
    private void updateAndBroadcastGameState() {
        gameStateLock.lock();
        try {
            // MAP then Save Update game state
            GameState gameState = GameStateMapper.toDTO(this.gameStateDOM);
            broadcast(gameState, GAME_STATE);
            LobbyService.storeLobby(lobby);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            gameStateLock.unlock();
        }
    }
    
    
    // this is the general structure of any method that mutates the Game State anywhere from this point on
    // once the state is mutated you have to call the Lobby Persistence to save the changes to json file by lobbyId
    private void updateAndBroadcastFinishedGame() {
        gameStateLock.lock();
        try {
            // RandomizePlayers or let Admin decide --> we randomize
            LinkedHashSet<Integer> leaderboard = generalLeaderboard();
            // MAP then Save Update game state
            GameState gameState = GameStateMapper.toDTO(this.gameStateDOM);
            lobby.setGameState(gameState);
            GameEnd gameEnd = new GameEnd(this.lobby, leaderboard);
            broadcast(gameEnd, GAME_END);
            LobbyService.storeFinishedGame(gameEnd);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            gameStateLock.unlock();
        }
        // Remove the lobby from the Repository as the json file is saved for recent games request 
        this.clientHandlersRepository.getLobbyParticipants().remove(lobby.getLobbyId());
    }

    private @NotNull LinkedHashSet<Integer> generalLeaderboard() {
        gameStateDOM.shufflePlayers();
        LinkedHashSet<Integer> leaderboard = new LinkedHashSet<>();
        for (PlayerDOM player : gameStateDOM.getPlayers()) {
            leaderboard.add(player.getPlayerId());
        }
        return leaderboard;
    }

    /**
     * Start.
     */
    public void start() {
        if (lobby.getGameState().getPlayers().size() > 2) {
            this.IsRunning = true;
        }
        executorService.submit(this);
    }

    /**
     * Stop.
     */
    public void stop() {
        this.IsRunning = false;
        executorService.shutdownNow();  // Stops the game loop
    }

    private void removeDeadPlayers() {
        // Thread-Safe removal
        participantHandlers.removeIf(participantHandler -> !participantHandler.isAlive());
        for (ParticipantHandler participantHandler : participantHandlers) {
            if (!participantHandler.isAlive()) {
                participantHandlers.remove(participantHandler);
                clientHandlersRepository.removeLobbyParticipant(lobby.getLobbyId(), participantHandler);
            }
        }
    }

    private void broadcast(Object content, Packet.Type packetType) {
        removeDeadPlayers();
        for (ParticipantHandler participantHandler : this.participantHandlers) {
            if (participantHandler.isAlive()) {
                Packet packetOut = getPacket(content, packetType);
                try {
                    participantHandler.sendPacket(packetOut);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Packet getPacket(Object content, Packet.Type PacketType) {
        Packet packet = Packet.builder().withType(PacketType).withContent(content)
                .build();
        return packet;
    }
    
    void updateParticipantsRepository(){
        this.participantHandlers.addAll(this.clientHandlersRepository.getLobbyParticipants().get(this.lobby.getLobbyId()));
    }
}