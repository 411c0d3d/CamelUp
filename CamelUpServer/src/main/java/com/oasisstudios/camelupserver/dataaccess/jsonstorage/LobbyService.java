package com.oasisstudios.camelupserver.dataaccess.jsonstorage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.oasisstudios.camelupserver.dataaccess.dto.*;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.oasisstudios.camelupserver.dataaccess.jsonstorage.JsonStorageHandler.objectMapper;

/**
 * The Lobby service.
 */

public class LobbyService {
    private JsonStorageHandler jsonStorageHandler = new JsonStorageHandler();
    private static final String LOBBY_DIRECTORY = "src/main/resources/data/lobbies/";

    /**
     * Save lobby.
     *
     * @param lobby the lobby dto
     * @throws IOException the io exception
     */
    public static void storeLobby(Lobby lobby) throws IOException {
        int lobbyId = lobby.getLobbyId();
        String newFileName = LOBBY_DIRECTORY + "lobby_" + lobbyId + "_" + lobby.getGameState().getGamePhase() + ".json";
        File newFile = new File(newFileName);

        // Check if a file exists for the same lobbyId but a different gamePhase
        File[] files = new File(LOBBY_DIRECTORY).listFiles((dir, name) -> name.startsWith(lobbyId + "_"));

        if (files != null) {
            for (File file : files) {
                if (!file.getName().equals(newFile.getName())) {
                    file.delete();  // Delete the old file with the previous gamePhase
                }
            }
        }
        // Save the new state with the current gamePhase
        newFile.getParentFile().mkdirs();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(newFile, lobby);
        cleanupOldFiles(lobbyId);
    }

    public static void storeFinishedGame(GameEnd recentGame) throws IOException {
        int lobbyId = recentGame.getLobby().getLobbyId();
        String newFileName = LOBBY_DIRECTORY + "lobby_" + lobbyId + "_" + "_finishedgame" + ".json";
        File newFile = new File(newFileName);
        // Check if a file exists for the same lobbyId but a different gamePhase
        File[] files = new File(LOBBY_DIRECTORY).listFiles((dir, name) -> name.startsWith(lobbyId + "_"));

        if (files != null) {
            for (File file : files) {
                if (!file.getName().equals(newFile.getName())) {
                    file.delete(); // Delete the old file with the previous gamePhase
                }
            }
        }
        // Save the new state with the current gamePhase
        newFile.getParentFile().mkdirs();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(newFile, recentGame);
        cleanupOldFiles(lobbyId);
    }

    // Delete old game state files based on the lobbyId
    private static void cleanupOldFiles(int lobbyId) {
        File dir = new File(LOBBY_DIRECTORY);
        File[] files = dir.listFiles((dir1, name) -> name.startsWith(String.valueOf(lobbyId)) && name.endsWith(".json") || name.endsWith("-1"));
        // clean up only lobbies that are older than 4 days
        if (files != null) {
            java.util.Arrays.sort(files, (file1, file2) -> Long.compare(file2.lastModified(), file1.lastModified()));
            for (int i = 1; i < files.length; i++) {
                boolean delete = files[i].delete();
                if (delete) {
                    System.out.println("Deleted " + files[i].getName());
                }
            }
        }
    }


    /**
     * Join lobby as spectator boolean.
     *
     * @param lobbyId     the lobby id
     * @param spectatorId the spectator id
     * @return the boolean
     */
    public boolean joinLobbyAsSpectator(int lobbyId, int spectatorId) {
        try {
            Lobby lobby = readLobbyByFileName(lobbyId);
            if (lobby != null) {
                List<Integer> array = lobby.getObserverIds();
                var gamePhase = lobby.getGameState().getGamePhase();
                array.add(spectatorId);
                lobby.setObserverIds(array);
                jsonStorageHandler.saveJsonToFile(lobby, getLobbyFilePath(lobbyId, gamePhase));
                return true;
            }
        } catch (Exception e) {
            JsonStorageHandler.logger.error(e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * Join lobby as spectator boolean.
     *
     * @param lobbyId     the lobby id
     * @param spectatorId the spectator id
     * @return the boolean
     */
    public boolean leaveLobbyAsSpectator(int lobbyId, int spectatorId) {
        try {
            Lobby lobby = readLobbyByFileName(lobbyId);
            if (lobby != null) {
                List<Integer> array = lobby.getObserverIds();
                array.remove(spectatorId);
                lobby.setObserverIds(array);
                storeLobby(lobby);
                return true;
            }
        } catch (Exception e) {
            JsonStorageHandler.logger.error(e.getMessage());
            return false;
        }
        return false;
    }


    /**
     * Join lobby as player boolean.
     *
     * @param lobbyId the lobby id
     * @param player  the player
     * @return the boolean
     */
    public boolean joinLobbyAsPlayer(int lobbyId, Player player) {
        try {
            Lobby lobby = readLobbyByFileName(lobbyId);
            if (lobby != null) {
                var gameState = lobby.getGameState();
                var players = gameState.getPlayers();
                if (players.size() <= gameState.getGameConfig().getPlayerCount()) {
                    player.setState(Player.State.PLAYING);
                    players.add(player);
                    gameState.setPlayers(players);
                    lobby.setGameState(gameState);
                    storeLobby(lobby);
                    ;
                }
            }
        } catch (Exception e) {
            JsonStorageHandler.logger.error(e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * Read lobby by file name lobby.
     *
     * @param lobbyId   the lobby id
     * @param gamePhase the game phase
     * @return the lobby
     * @throws Exception the exception
     */
    public static Lobby readLobbyByFileName(int lobbyId, String gamePhase) throws Exception {
        File directory = new File(LOBBY_DIRECTORY);
        directory.getParentFile().mkdirs();
        File[] jsonFiles = directory.listFiles();

        if (jsonFiles == null) {
            return null;
        }

        for (File file : jsonFiles) {
            String filename = file.getName().replace("lobby", "").replace(".json", "");

            if (filename.contains(String.valueOf(lobbyId))) {
                if (gamePhase != null && !gamePhase.equals("finished") && !filename.contains(gamePhase)) {
                    continue;
                }
                return objectMapper.readValue(file, Lobby.class);
            }
        }
        return null;
    }

    public static GameConfig readGameConfigByFileName(String filePath) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            return objectMapper.readValue(file, GameConfig.class);
        }
        return null;
    }

    /**
     * Read lobby by file name lobby.
     *
     * @param lobbyId the lobby id
     * @return the lobby
     * @throws Exception the exception
     */
    public static Lobby readLobbyByFileName(int lobbyId) throws Exception {
        return readLobbyByFileName(lobbyId, null); // Default gamePhase is null
    }

    /**
     * Read running lobbies lobby list.
     *
     * @param max describes the maximum amount of lobbies that are to be returned
     * @return LobbyListDTO, which is a list of lobbies
     */
    public LobbyList readRunningLobbies(int max) {
        File directory = new File(LOBBY_DIRECTORY);
        directory.getParentFile().mkdirs();
        File[] jsonFiles = directory.listFiles();
        if (jsonFiles == null || jsonFiles.length == 0) {
            return new LobbyList(); // Return empty DTO if no files
        }

        // Limit the number of files to process
        max = Math.min(max, jsonFiles.length);

        List<Lobby> lobbies = new ArrayList<>(max);
        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 0; i < max; i++) {
            if (!jsonFiles[i].getName().toLowerCase().contains("finished".toLowerCase())) {
                try {
                    Lobby lobby = objectMapper.readValue(jsonFiles[i], Lobby.class);
                    lobbies.add(lobby);
                } catch (IOException e) {
                    System.err.println("Failed to read file: " + jsonFiles[i].getName() + " - " + e.getMessage());
                }
            }
        }

        // Return the populated DTO
        LobbyList lobbyList = new LobbyList();
        lobbyList.setLobbies(lobbies);
        return lobbyList;
    }


    /**
     * Read finished lobbies lobby list.
     *
     * @param max describes the maximum amount of lobbies that are to be returned
     * @return LobbyList, which is a list of lobbies that are finished
     */
    public RecentGames readFinishedLobbies(int max) {
        File directory = new File(LOBBY_DIRECTORY);
        directory.getParentFile().mkdirs();
        File[] jsonFiles = directory.listFiles();
        if (jsonFiles == null || jsonFiles.length == 0) {
            return new RecentGames(); // Return empty DTO if no files
        }

        // Limit the number of files to process
        max = Math.min(max, jsonFiles.length);

        List<RecentGame> lobbies = new ArrayList<>(max);
        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 0; i < max; i++) {
            if (jsonFiles[i].getName().toLowerCase().contains("finishedgame".toLowerCase())) {
                try {
                    RecentGame lobby = objectMapper.readValue(jsonFiles[i], RecentGame.class);
                    lobbies.add(lobby);
                } catch (IOException e) {
                    System.err.println("Failed to read file: " + jsonFiles[i].getName() + " - " + e.getMessage());
                }
            }
        }

        // Return the populated DTO
        RecentGames lobbyList = new RecentGames();
        lobbyList.setLobbies(lobbies);
        return lobbyList;
    }

    private @NotNull String getLobbyFilePath(int lobbyId, GameState.GamePhase gamePhase) {
        return LOBBY_DIRECTORY + "lobby_" + lobbyId + "_" + gamePhase.getValue() + ".json";
    }
}
