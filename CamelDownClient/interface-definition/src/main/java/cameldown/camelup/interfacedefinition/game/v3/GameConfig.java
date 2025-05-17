package cameldown.camelup.interfacedefinition.game.v3;

import java.util.*;
import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


/**
 * gameConfig
 * <p>
 * The configuration of a game. This data is never sent as a separate package, only as part of the gameState package. This is the format of the configuration file that a server admin can load and save.
 */
@Generated("jsonschema2pojo")
@Data
public class GameConfig {
    /**
     * Instantiates a new Game config.
     *
     * @param playerCount          the player count
     * @param numberOfSpaces       the number of spaces
     * @param camels               the camels
     * @param diceFaces            the dice faces
     * @param numberOfBettingCards the number of betting cards
     * @param thinkingTime         the thinking time
     * @param visualizationTime    the visualization time
     * @param illegalMovePenalty   the illegal move penalty
     * @param maxGameDuration      the max game duration
     * @param maxTurns             the max turns
     */
    public GameConfig(Integer playerCount, Integer numberOfSpaces, List<Camel> camels, Integer diceFaces, Integer numberOfBettingCards, Integer thinkingTime, Integer visualizationTime, IllegalMovePenalty illegalMovePenalty, Integer maxGameDuration, Integer maxTurns) {
        this.playerCount = playerCount;
        this.numberOfSpaces = numberOfSpaces;
        this.camels = camels;
        this.diceFaces = diceFaces;
        this.numberOfBettingCards = numberOfBettingCards;
        this.thinkingTime = thinkingTime;
        this.visualizationTime = visualizationTime;
        this.illegalMovePenalty = illegalMovePenalty;
        this.maxGameDuration = maxGameDuration;
        this.maxTurns = maxTurns;
    }

    /**
     * Instantiates a new Game config.
     */
    public GameConfig(){}
    /**
     * The maximum number of players in a game
     * (Required)
     */
    @SerializedName("playerCount")
    @Expose
    @DecimalMin("2")
    @DecimalMax("6")
    @NotNull
    private Integer playerCount;
    /**
     * Die Anzahl an Feldern auf dem Spielfeld
     * (Required)
     */
    @SerializedName("numberOfSpaces")
    @Expose
    @DecimalMin("3")
    @DecimalMax("32766")
    @NotNull
    private Integer numberOfSpaces;
    /**
     * Eine Liste von Kamelen die sich im Spiel befinden
     * (Required)
     */
    @SerializedName("camels")
    @Expose
    @Size(min = 4, max = 32767)
    @Valid
    @NotNull
    private List<Camel> camels = new ArrayList<>();
    /**
     * Die Anzahl an Seiten die ein Würfel hat
     * (Required)
     */
    @SerializedName("diceFaces")
    @Expose
    @DecimalMin("1")
    @DecimalMax("10922")
    @NotNull
    private Integer diceFaces;
    /**
     * Die Anzahl an Wettkarten die jedes Kamel hat
     * (Required)
     */
    @SerializedName("numberOfBettingCards")
    @Expose
    @DecimalMin("3")
    @DecimalMax("32767")
    @NotNull
    private Integer numberOfBettingCards;
    /**
     * Die Zeit die ein Spieler zum überlegen hat in Millisekunden
     * (Required)
     */
    @SerializedName("thinkingTime")
    @Expose
    @DecimalMin("1")
    @DecimalMax("2147483647")
    @NotNull
    private Integer thinkingTime;
    /**
     * Die Zeit die die Visualisierung eines Zuges maximal dauern darf in Millisekunden
     * (Required)
     */
    @SerializedName("visualizationTime")
    @Expose
    @DecimalMin("1")
    @DecimalMax("2147483647")
    @NotNull
    private Integer visualizationTime;
    /**
     * Die Strafe die ein Spieler bekommt wenn er einen ungültigen Zug macht oder zu lange braucht
     * (Required)
     */
    @SerializedName("illegalMovePenalty")
    @Expose
    @NotNull
    private GameConfig.IllegalMovePenalty illegalMovePenalty;
    /**
     * Die maximale Dauer eines Spiels in Millisekunden
     * (Required)
     */
    @SerializedName("maxGameDuration")
    @Expose
    @DecimalMin("1")
    @DecimalMax("2147483647")
    @NotNull
    private Integer maxGameDuration;
    /**
     * Die maximale Anzahl an Runden die ein Spiel haben kann
     * (Required)
     */
    @SerializedName("maxTurns")
    @Expose
    @DecimalMin("1")
    @DecimalMax("32767")
    @NotNull
    private Integer maxTurns;

    /**
     * Builder game config . game config builder base.
     *
     * @return the game config . game config builder base
     */
    public static GameConfig.GameConfigBuilderBase builder() {
        return new GameConfig.GameConfigBuilder();
    }

    /**
     * The maximum number of players in a game
     * (Required)
     *
     * @return the player count
     */
    public Integer getPlayerCount() {
        return playerCount;
    }

    /**
     * The number of spaces on the board
     * (Required)
     *
     * @return the number of spaces
     */
    public Integer getNumberOfSpaces() {
        return numberOfSpaces;
    }

    /**
     * A list of camels in the game
     * (Required)
     *
     * @return the camels
     */
    public List<Camel> getCamels() {
        return camels;
    }

    /**
     * The number of faces a dice has
     * (Required)
     *
     * @return the dice faces
     */
    public Integer getDiceFaces() {
        return diceFaces;
    }

    /**
     * The number of betting cards each camel has
     * (Required)
     *
     * @return the number of betting cards
     */
    public Integer getNumberOfBettingCards() {
        return numberOfBettingCards;
    }

    /**
     * The time a player has to think in milliseconds
     * (Required)
     *
     * @return the thinking time
     */
    public Integer getThinkingTime() {
        return thinkingTime;
    }

    /**
     * The time a visualization of a move is allowed to take in milliseconds
     * (Required)
     *
     * @return the visualization time
     */
    public Integer getVisualizationTime() {
        return visualizationTime;
    }

    /**
     * The penalty a player gets when making an illegal move or taking too long
     * (Required)
     *
     * @return the illegal move penalty
     */
    public GameConfig.IllegalMovePenalty getIllegalMovePenalty() {
        return illegalMovePenalty;
    }

    /**
     * The maximum duration of a game in milliseconds
     * (Required)
     *
     * @return the max game duration
     */
    public Integer getMaxGameDuration() {
        return maxGameDuration;
    }

    /**
     * The maximum number of rounds a game can have
     * (Required)
     *
     * @return the max turns
     */
    public Integer getMaxTurns() {
        return maxTurns;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(GameConfig.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("playerCount");
        sb.append('=');
        sb.append(((this.playerCount == null) ? "<null>" : this.playerCount));
        sb.append(',');
        sb.append("numberOfSpaces");
        sb.append('=');
        sb.append(((this.numberOfSpaces == null) ? "<null>" : this.numberOfSpaces));
        sb.append(',');
        sb.append("camels");
        sb.append('=');
        sb.append(((this.camels == null) ? "<null>" : this.camels));
        sb.append(',');
        sb.append("diceFaces");
        sb.append('=');
        sb.append(((this.diceFaces == null) ? "<null>" : this.diceFaces));
        sb.append(',');
        sb.append("numberOfBettingsCards");
        sb.append('=');
        sb.append(((this.numberOfBettingCards == null) ? "<null>" : this.numberOfBettingCards));
        sb.append(',');
        sb.append("thinkingTime");
        sb.append('=');
        sb.append(((this.thinkingTime == null) ? "<null>" : this.thinkingTime));
        sb.append(',');
        sb.append("visualizationTime");
        sb.append('=');
        sb.append(((this.visualizationTime == null) ? "<null>" : this.visualizationTime));
        sb.append(',');
        sb.append("illegalMovePenalty");
        sb.append('=');
        sb.append(((this.illegalMovePenalty == null) ? "<null>" : this.illegalMovePenalty));
        sb.append(',');
        sb.append("maxGameDuration");
        sb.append('=');
        sb.append(((this.maxGameDuration == null) ? "<null>" : this.maxGameDuration));
        sb.append(',');
        sb.append("maxRounds");
        sb.append('=');
        sb.append(((this.maxTurns == null) ? "<null>" : this.maxTurns));
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
        result = ((result * 31) + ((this.camels == null) ? 0 : this.camels.hashCode()));
        result = ((result * 31) + ((this.playerCount == null) ? 0 : this.playerCount.hashCode()));
        result = ((result * 31) + ((this.visualizationTime == null) ? 0 : this.visualizationTime.hashCode()));
        result = ((result * 31) + ((this.numberOfBettingCards == null) ? 0 : this.numberOfBettingCards.hashCode()));
        result = ((result * 31) + ((this.maxTurns == null) ? 0 : this.maxTurns.hashCode()));
        result = ((result * 31) + ((this.numberOfSpaces == null) ? 0 : this.numberOfSpaces.hashCode()));
        result = ((result * 31) + ((this.illegalMovePenalty == null) ? 0 : this.illegalMovePenalty.hashCode()));
        result = ((result * 31) + ((this.maxGameDuration == null) ? 0 : this.maxGameDuration.hashCode()));
        result = ((result * 31) + ((this.diceFaces == null) ? 0 : this.diceFaces.hashCode()));
        result = ((result * 31) + ((this.thinkingTime == null) ? 0 : this.thinkingTime.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof GameConfig) == false) {
            return false;
        }
        GameConfig rhs = ((GameConfig) other);
        return (((((((((((this.camels == rhs.camels) || ((this.camels != null) && this.camels.equals(rhs.camels))) && ((this.playerCount == rhs.playerCount) || ((this.playerCount != null) && this.playerCount.equals(rhs.playerCount)))) && ((this.visualizationTime == rhs.visualizationTime) || ((this.visualizationTime != null) && this.visualizationTime.equals(rhs.visualizationTime)))) && ((this.numberOfBettingCards == rhs.numberOfBettingCards) || ((this.numberOfBettingCards != null) && this.numberOfBettingCards.equals(rhs.numberOfBettingCards)))) && ((this.maxTurns == rhs.maxTurns) || ((this.maxTurns != null) && this.maxTurns.equals(rhs.maxTurns)))) && ((this.numberOfSpaces == rhs.numberOfSpaces) || ((this.numberOfSpaces != null) && this.numberOfSpaces.equals(rhs.numberOfSpaces)))) && ((this.illegalMovePenalty == rhs.illegalMovePenalty) || ((this.illegalMovePenalty != null) && this.illegalMovePenalty.equals(rhs.illegalMovePenalty)))) && ((this.maxGameDuration == rhs.maxGameDuration) || ((this.maxGameDuration != null) && this.maxGameDuration.equals(rhs.maxGameDuration)))) && ((this.diceFaces == rhs.diceFaces) || ((this.diceFaces != null) && this.diceFaces.equals(rhs.diceFaces)))) && ((this.thinkingTime == rhs.thinkingTime) || ((this.thinkingTime != null) && this.thinkingTime.equals(rhs.thinkingTime))));
    }

    /**
     * The type Game config builder.
     */
    public static class GameConfigBuilder
            extends GameConfig.GameConfigBuilderBase<GameConfig> {


        /**
         * Instantiates a new Game config builder.
         */
        public GameConfigBuilder() {
            super();
        }

    }

    /**
     * The type Game config builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class GameConfigBuilderBase<T extends GameConfig> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Game config builder base.
         */
        @SuppressWarnings("unchecked")
        public GameConfigBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(GameConfig.GameConfigBuilder.class)) {
                this.instance = ((T) new GameConfig());
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
         * With player count game config . game config builder base.
         *
         * @param playerCount the player count
         * @return the game config . game config builder base
         */
        public GameConfig.GameConfigBuilderBase withPlayerCount(Integer playerCount) {
            ((GameConfig) this.instance).playerCount = playerCount;
            return this;
        }

        /**
         * With number of spaces game config . game config builder base.
         *
         * @param numberOfSpaces the number of spaces
         * @return the game config . game config builder base
         */
        public GameConfig.GameConfigBuilderBase withNumberOfSpaces(Integer numberOfSpaces) {
            ((GameConfig) this.instance).numberOfSpaces = numberOfSpaces;
            return this;
        }

        /**
         * With camels game config . game config builder base.
         *
         * @param camels the camels
         * @return the game config . game config builder base
         */
        public GameConfig.GameConfigBuilderBase withCamels(List<Camel> camels) {
            ((GameConfig) this.instance).camels = camels;
            return this;
        }

        /**
         * With dice faces game config . game config builder base.
         *
         * @param diceFaces the dice faces
         * @return the game config . game config builder base
         */
        public GameConfig.GameConfigBuilderBase withDiceFaces(Integer diceFaces) {
            ((GameConfig) this.instance).diceFaces = diceFaces;
            return this;
        }

        /**
         * With number of bettings cards game config . game config builder base.
         *
         * @param numberOfBettingsCards the number of bettings cards
         * @return the game config . game config builder base
         */
        public GameConfig.GameConfigBuilderBase withNumberOfBettingsCards(Integer numberOfBettingsCards) {
            ((GameConfig) this.instance).numberOfBettingCards = numberOfBettingsCards;
            return this;
        }

        /**
         * With thinking time game config . game config builder base.
         *
         * @param thinkingTime the thinking time
         * @return the game config . game config builder base
         */
        public GameConfig.GameConfigBuilderBase withThinkingTime(Integer thinkingTime) {
            ((GameConfig) this.instance).thinkingTime = thinkingTime;
            return this;
        }

        /**
         * With visualization time game config . game config builder base.
         *
         * @param visualizationTime the visualization time
         * @return the game config . game config builder base
         */
        public GameConfig.GameConfigBuilderBase withVisualizationTime(Integer visualizationTime) {
            ((GameConfig) this.instance).visualizationTime = visualizationTime;
            return this;
        }

        /**
         * With illegal move penalty game config . game config builder base.
         *
         * @param illegalMovePenalty the illegal move penalty
         * @return the game config . game config builder base
         */
        public GameConfig.GameConfigBuilderBase withIllegalMovePenalty(GameConfig.IllegalMovePenalty illegalMovePenalty) {
            ((GameConfig) this.instance).illegalMovePenalty = illegalMovePenalty;
            return this;
        }

        /**
         * With max game duration game config . game config builder base.
         *
         * @param maxGameDuration the max game duration
         * @return the game config . game config builder base
         */
        public GameConfig.GameConfigBuilderBase withMaxGameDuration(Integer maxGameDuration) {
            ((GameConfig) this.instance).maxGameDuration = maxGameDuration;
            return this;
        }

        /**
         * With max rounds game config . game config builder base.
         *
         * @param maxRounds the max rounds
         * @return the game config . game config builder base
         */
        public GameConfig.GameConfigBuilderBase withMaxRounds(Integer maxRounds) {
            ((GameConfig) this.instance).maxTurns = maxRounds;
            return this;
        }

    }


    /**
     * The penalty a player gets when making an illegal move or taking too long
     */
    @Generated("jsonschema2pojo")
    public enum IllegalMovePenalty {

        /**
         * Forfeit current stage illegal move penalty.
         */
        @SerializedName("forfeitCurrentStage")
        FORFEIT_CURRENT_STAGE("forfeitCurrentStage"),
        /**
         * Forfeit game illegal move penalty.
         */
        @SerializedName("forfeitGame")
        FORFEIT_GAME("forfeitGame");
        private final String value;
        private final static Map<String, GameConfig.IllegalMovePenalty> CONSTANTS = new HashMap<String, GameConfig.IllegalMovePenalty>();

        static {
            for (GameConfig.IllegalMovePenalty c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        IllegalMovePenalty(String value) {
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
        public String value() {
            return this.value;
        }

        /**
         * From value game config . illegal move penalty.
         *
         * @param value the value
         * @return the game config . illegal move penalty
         */
        public static GameConfig.IllegalMovePenalty fromValue(String value) {
            GameConfig.IllegalMovePenalty constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
