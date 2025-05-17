package com.oasisstudios.camelupserver.dataaccess.dto;

//import static org.mockito.Answers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


/**
 * gameState
 * <p>
 * The current state of the game, sent only by the server during an active game upon updates.
 */
@Generated("jsonschema2pojo")
@Data
public class GameState {
    
    /**
     * Instantiates a new Game state.
     *
     * @param gamePhase         the game phase
     * @param boardSpaces       the board spaces
     * @param gameConfig        the game config
     * @param rolledDice        the rolled dice
     * @param bettingCards      the betting cards
     * @param players           the players
     * @param turns             the turns
     * @param gameDuration      the game duration
     * @param moveTimeRemaining the move time remaining
     * @param finalBets         the final bets
     */
    public GameState(GamePhase gamePhase, List<BoardSpace> boardSpaces, GameConfig gameConfig, List<RolledDice> rolledDice, List<BettingCards> bettingCards, List<Player> players, Integer turns, Integer gameDuration, Integer moveTimeRemaining, FinalBets finalBets) {
        this.gamePhase = gamePhase;
        this.boardSpaces = boardSpaces;
        this.gameConfig = gameConfig;
        this.rolledDice = rolledDice;
        this.bettingCards = bettingCards;
        this.players = players;
        this.turns = turns;
        this.gameDuration = gameDuration;
        this.moveTimeRemaining = moveTimeRemaining;
        this.finalBets = finalBets;
    }
    
    public GameState(){}
    
    /**
     * Indicates the current phase of the game. The sequence is always created -> visualizing -> (playing -> visualizing)* -> (paused -> visualizing -> playing) -> finished.
     * created: The game has been created but not yet started.
     * playing: The currently active player must make their move.
     * visualizing: The clients are visualizing the game.
     * paused: The game is paused and waiting to be resumed. This phase is always followed by visualizing.
     * finished: The game is over or has been aborted.",
     * (Required)
     */
    @SerializedName("gamePhase")
    @Expose
    @NotNull
    private GameState.GamePhase gamePhase;
    /**
     * A list of all spaces on the game board and the finish line. The positions [0, n-1] in the array are all spaces on the game board in sequence. The n-th position is for the camel that crossed the finish line first and thus ended the game. Therefore, no player tile can be placed there and the camel cannot be moved further once it is on this space.",
     * (Required)
     */
    @SerializedName("boardSpaces")
    @Expose
    @Valid
    @NotNull
    private List<BoardSpace> boardSpaces = new ArrayList<BoardSpace>();
    /**
     * gameConfig
     * <p>
     * The configuration of a game. This data is never sent as a separate package, only as part of the gameState package. This is the format of the configuration file that a server admin can load and save.
     * (Required)
     */
    @SerializedName("gameConfig")
    @Expose
    @Valid
    @NotNull
    private GameConfig gameConfig;
    /**
     * The list of rolled dice and their results. The list is in the order the dice were rolled. For the backward-moving camels, only the ID of the rolled camel is stored.
     * (Required)
     */
    @SerializedName("rolledDice")
    @Expose
    @Size(min = 0)
    @Valid
    @NotNull
    private List<RolledDice> rolledDice = new ArrayList<>();
    /**
     * A list of camelId and amount pairs indicating how many betting cards are left for a camel in this stage.
     * (Required)
     */
    @SerializedName("bettingCards")
    @Expose
    @Size(min = 0)
    @Valid
    @NotNull
    private List<BettingCards> bettingCards = new ArrayList<>();
    /**
     * A list of all players in this game. The first player in the list is the active player, the second is the next, and so on.
     * (Required)
     */
    @SerializedName("players")
    @Expose
    @Size(min = 2, max = 6)
    @Valid
    @NotNull
    private List<Player> players = new ArrayList<Player>();
    /**
     * The number of turns already played in the game.
     * It starts at 0 and is incremented by one in the transition from phase PLAYING to VISUALISING.
     * (Required)
     */
    @SerializedName("turns")
    @Expose
    @DecimalMin("0")
    @NotNull
    private Integer turns;
    /**
     * How long the game has been running in milliseconds",
     * (Required)
     */
    @SerializedName("gameDuration")
    @Expose
    @DecimalMin("0")
    @NotNull
    private Integer gameDuration;
    /**
     * How much time the active player has left to make their move in milliseconds",
     * (Required)
     */
    @SerializedName("moveTimeRemaining")
    @Expose
    @DecimalMin("0")
    @NotNull
    private Integer moveTimeRemaining;
    /**
     * The two stacks for the final bets.",
     * (Required)
     */
    @SerializedName("finalBets")
    @Expose
    @Valid
    @NotNull
    private FinalBets finalBets;

    /**
     * Builder game state . game state builder base.
     *
     * @return the game state . game state builder base
     */
    public static GameState.GameStateBuilderBase builder() {
        return new GameState.GameStateBuilder();
    }

    /**
     * Indicates the current phase of the game. The sequence is always created -> visualizing -> (playing -> visualizing)* -> (paused -> visualizing -> playing) -> finished.
     * created: The game has been created but not yet started.
     * playing: The currently active player must make their move.
     * visualizing: The clients are visualizing the game.
     * paused: The game is paused and waiting to be resumed. This phase is always followed by visualizing.
     * finished: The game is over or has been aborted.",
     * (Required)
     *
     * @return the game phase
     */
    public GameState.GamePhase getGamePhase() {
        return gamePhase;
    }

    /**
     * A list of all spaces on the game board and the finish line. The positions [0, n-1] in the array are all spaces on the game board in sequence. The n-th position is for the camel that crossed the finish line first and thus ended the game. Therefore, no player tile can be placed there and the camel cannot be moved further once it is on this space.",
     * (Required)
     *
     * @return the board spaces
     */
    public List<BoardSpace> getBoardSpaces() {
        return boardSpaces;
    }

    /**
     * gameConfig
     * <p>
     * The configuration of a game. This data is never sent as a separate package, only as part of the gameState package. This is the format of the configuration file that a server admin can load and save.
     * (Required)
     *
     * @return the game config
     */
    public GameConfig getGameConfig() {
        return gameConfig;
    }

    /**
     * The list of rolled dice and their results. The list is in the order the dice were rolled. For the backward-moving camels, only the ID of the rolled camel is stored.
     * (Required)
     *
     * @return the rolled dice
     */
    public List<RolledDice> getRolledDice() {
        return rolledDice;
    }

    /**
     * A list of camelId and amount pairs indicating how many betting cards are left for a camel in this stage.
     * (Required)
     *
     * @return the betting cards
     */
    public List<BettingCards> getBettingCards() {
        return bettingCards;
    }

    /**
     * A list of all players in this game. The first player in the list is the active player, the second is the next, and so on.
     * (Required)
     *
     * @return the players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * The number of rounds already played in the game. It starts at 0 and is incremented by one after each stage scoring.
     * (Required)
     *
     * @return the turns
     */
    public Integer getTurns() {
        return turns;
    }

    /**
     * How long the game has been running in milliseconds
     * (Required)
     *
     * @return the game duration
     */
    public Integer getGameDuration() {
        return gameDuration;
    }

    /**
     * How much time the active player has left to make their move in milliseconds
     * (Required)
     *
     * @return the move time remaining
     */
    public Integer getMoveTimeRemaining() {
        return moveTimeRemaining;
    }

    /**
     * The two stacks for the final bets.
     * (Required)
     *
     * @return the final bets
     */
    public FinalBets getFinalBets() {
        return finalBets;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(GameState.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("gamePhase");
        sb.append('=');
        sb.append(((this.gamePhase == null) ? "<null>" : this.gamePhase.getValue()));
        sb.append(',');
        sb.append("boardSpaces");
        sb.append('=');
        sb.append(((this.boardSpaces == null) ? "<null>" : this.boardSpaces));
        sb.append(',');
        sb.append("gameConfig");
        sb.append('=');
        sb.append(((this.gameConfig == null) ? "<null>" : this.gameConfig));
        sb.append(',');
        sb.append("rolledDice");
        sb.append('=');
        sb.append(((this.rolledDice == null) ? "<null>" : this.rolledDice));
        sb.append(',');
        sb.append("bettingCards");
        sb.append('=');
        sb.append(((this.bettingCards == null) ? "<null>" : this.bettingCards));
        sb.append(',');
        sb.append("players");
        sb.append('=');
        sb.append(((this.players == null) ? "<null>" : this.players));
        sb.append(',');
        sb.append("rounds");
        sb.append('=');
        sb.append(((this.turns == null) ? "<null>" : this.turns));
        sb.append(',');
        sb.append("gameDuration");
        sb.append('=');
        sb.append(((this.gameDuration == null) ? "<null>" : this.gameDuration));
        sb.append(',');
        sb.append("moveTimeRemaining");
        sb.append('=');
        sb.append(((this.moveTimeRemaining == null) ? "<null>" : this.moveTimeRemaining));
        sb.append(',');
        sb.append("finalBets");
        sb.append('=');
        sb.append(((this.finalBets == null) ? "<null>" : this.finalBets));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.gameConfig == null) ? 0 : this.gameConfig.hashCode()));
        result = ((result * 31) + ((this.boardSpaces == null) ? 0 : this.boardSpaces.hashCode()));
        result = ((result * 31) + ((this.gameDuration == null) ? 0 : this.gameDuration.hashCode()));
        result = ((result * 31) + ((this.players == null) ? 0 : this.players.hashCode()));
        result = ((result * 31) + ((this.rolledDice == null) ? 0 : this.rolledDice.hashCode()));
        result = ((result * 31) + ((this.gamePhase == null) ? 0 : this.gamePhase.hashCode()));
        result = ((result * 31) + ((this.bettingCards == null) ? 0 : this.bettingCards.hashCode()));
        result = ((result * 31) + ((this.finalBets == null) ? 0 : this.finalBets.hashCode()));
        result = ((result * 31) + ((this.moveTimeRemaining == null) ? 0 : this.moveTimeRemaining.hashCode()));
        result = ((result * 31) + ((this.turns == null) ? 0 : this.turns.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GameState) == false) {
            return false;
        }
        GameState rhs = ((GameState) other);
        return (((((((((((this.gameConfig == rhs.gameConfig) || ((this.gameConfig != null) && this.gameConfig.equals(rhs.gameConfig))) && ((this.boardSpaces == rhs.boardSpaces) || ((this.boardSpaces != null) && this.boardSpaces.equals(rhs.boardSpaces)))) && ((this.gameDuration == rhs.gameDuration) || ((this.gameDuration != null) && this.gameDuration.equals(rhs.gameDuration)))) && ((this.players == rhs.players) || ((this.players != null) && this.players.equals(rhs.players)))) && ((this.rolledDice == rhs.rolledDice) || ((this.rolledDice != null) && this.rolledDice.equals(rhs.rolledDice)))) && ((this.gamePhase == rhs.gamePhase) || ((this.gamePhase != null) && this.gamePhase.equals(rhs.gamePhase)))) && ((this.bettingCards == rhs.bettingCards) || ((this.bettingCards != null) && this.bettingCards.equals(rhs.bettingCards)))) && ((this.finalBets == rhs.finalBets) || ((this.finalBets != null) && this.finalBets.equals(rhs.finalBets)))) && ((this.moveTimeRemaining == rhs.moveTimeRemaining) || ((this.moveTimeRemaining != null) && this.moveTimeRemaining.equals(rhs.moveTimeRemaining)))) && ((this.turns == rhs.turns) || ((this.turns != null) && this.turns.equals(rhs.turns))));
    }


    /**
     * Indicates the current phase of the game. The sequence is always created -> visualizing -> (playing -> visualizing)* -> (paused -> visualizing -> playing) -> finished.
     * created: The game has been created but not yet started.
     * playing: The currently active player must make their move.
     * visualizing: The clients are visualizing the game.
     * paused: The game is paused and waiting to be resumed. This phase is always followed by visualizing.
     * finished: The game is over or has been aborted.",
     */
    @Generated("jsonschema2pojo")
    public enum GamePhase {

        /**
         * Created game phase.
         */
        @SerializedName("created")
        CREATED("created"),
        /**
         * Playing game phase.
         */
        @SerializedName("playing")
        PLAYING("playing"),
        /**
         * Visualizing game phase.
         */
        @SerializedName("visualizing")
        VISUALIZING("visualizing"),
        /**
         * Paused game phase.
         */
        @SerializedName("paused")
        PAUSED("paused"),
        /**
         * Finished game phase.
         */
        @SerializedName("finished")
        FINISHED("finished");
        private final String value;
        private final static Map<String, GameState.GamePhase> CONSTANTS = new HashMap<String, GameState.GamePhase>();

        static {
            for (GameState.GamePhase c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        GamePhase(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        /**
         * Value string.
         *
         * @return the string
         */
        @JsonValue
        public String getValue() {
            return value;
        }

        /**
         * From value game config . illegal move penalty.
         *
         * @param value the value
         * @return the game config . illegal move penalty
         */
        @JsonCreator
        public static GameState.GamePhase fromValue(String value) {
            for (GameState.GamePhase penalty : GameState.GamePhase.values()) {
                if (penalty.value.equalsIgnoreCase(value)) {
                    return penalty;
                }
            }
            System.err.println("\"Unknown GamePhase:" + value);
            return null;
        }

    }

    /**
     * The type Game state builder.
     */
    public static class GameStateBuilder
        extends GameState.GameStateBuilderBase<GameState> {


        /**
         * Instantiates a new Game state builder.
         */
        public GameStateBuilder() {
            super();
        }

    }

    /**
     * The type Game state builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class GameStateBuilderBase<T extends GameState> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Game state builder base.
         */
        @SuppressWarnings("unchecked")
        public GameStateBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(GameState.GameStateBuilder.class)) {
                this.instance = ((T) new GameState());
            }
        }

        /**
         * Build t.
         *
         * @return the t
         */
        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        /**
         * With game phase game state . game state builder base.
         *
         * @param gamePhase the game phase
         * @return the game state . game state builder base
         */
        public GameState.GameStateBuilderBase withGamePhase(GameState.GamePhase gamePhase) {
            ((GameState) this.instance).gamePhase = gamePhase;
            return this;
        }

        /**
         * With board spaces game state . game state builder base.
         *
         * @param boardSpaces the board spaces
         * @return the game state . game state builder base
         */
        public GameState.GameStateBuilderBase withBoardSpaces(List<BoardSpace> boardSpaces) {
            ((GameState) this.instance).boardSpaces = boardSpaces;
            return this;
        }

        /**
         * With game config game state . game state builder base.
         *
         * @param gameConfig the game config
         * @return the game state . game state builder base
         */
        public GameState.GameStateBuilderBase withGameConfig(GameConfig gameConfig) {
            ((GameState) this.instance).gameConfig = gameConfig;
            return this;
        }

        /**
         * With rolled dice game state . game state builder base.
         *
         * @param rolledDice the rolled dice
         * @return the game state . game state builder base
         */
        public GameState.GameStateBuilderBase withRolledDice(List<RolledDice> rolledDice) {
            ((GameState) this.instance).rolledDice = rolledDice;
            return this;
        }

        /**
         * With betting cards game state . game state builder base.
         *
         * @param bettingCards the betting cards
         * @return the game state . game state builder base
         */
        public GameState.GameStateBuilderBase withBettingCards(List<BettingCards> bettingCards) {
            ((GameState) this.instance).bettingCards = bettingCards;
            return this;
        }

        /**
         * With players game state . game state builder base.
         *
         * @param players the players
         * @return the game state . game state builder base
         */
        public GameState.GameStateBuilderBase withPlayers(List<Player> players) {
            ((GameState) this.instance).players = players;
            return this;
        }

        /**
         * With rounds game state . game state builder base.
         *
         * @param rounds the rounds
         * @return the game state . game state builder base
         */
        public GameState.GameStateBuilderBase withRounds(Integer rounds) {
            ((GameState) this.instance).turns = rounds;
            return this;
        }

        /**
         * With game duration game state . game state builder base.
         *
         * @param gameDuration the game duration
         * @return the game state . game state builder base
         */
        public GameState.GameStateBuilderBase withGameDuration(Integer gameDuration) {
            ((GameState) this.instance).gameDuration = gameDuration;
            return this;
        }

        /**
         * With move time remaining game state . game state builder base.
         *
         * @param moveTimeRemaining the move time remaining
         * @return the game state . game state builder base
         */
        public GameState.GameStateBuilderBase withMoveTimeRemaining(Integer moveTimeRemaining) {
            ((GameState) this.instance).moveTimeRemaining = moveTimeRemaining;
            return this;
        }

        /**
         * With final bets game state . game state builder base.
         *
         * @param finalBets the final bets
         * @return the game state . game state builder base
         */
        public GameState.GameStateBuilderBase withFinalBets(FinalBets finalBets) {
            ((GameState) this.instance).finalBets = finalBets;
            return this;
        }

    }

}
