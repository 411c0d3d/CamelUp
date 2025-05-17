package com.oasisstudios.camelupclient.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import javax.annotation.processing.Generated;
import java.util.*;

/**
 * player
 * <p>
 * A specific player participating in the game
 */
@Generated("jsonschema2pojo")
@Data
public class Player {

    /**
     * Instantiates a new Player.
     *
     * @param playerId     the player id
     * @param name         the name
     * @param money        the money
     * @param bettingCards the betting cards
     * @param state        the state
     */
    public Player(Integer playerId, String name, Integer money, List<BettingCard> bettingCards, State state) {
        this.playerId = playerId;
        this.name = name;
        this.money = money;
        this.bettingCards = bettingCards;
        this.state = state;
    }

    /**
     * Instantiates a new Player.
     */
    public Player(){}

    /**
     * The unique Id of the player. This is equal to the clientId of the client.
     * (Required)
     */
    @SerializedName("playerId")
    @Expose
    @NotNull
    private Integer playerId;
    /**
     * The name of the player
     * (Required)
     */
    @SerializedName("name")
    @Expose
    @NotNull
    private String name;
    /**
     * The number of coins the player has
     * (Required)
     */
    @SerializedName("money")
    @Expose
    @DecimalMin("0")
    @NotNull
    private Integer money;
    /**
     * A list of betting cards the player has
     * (Required)
     */
    @SerializedName("bettingCards")
    @Expose
    @Size(min = 0)
    @Valid
    @NotNull
    private List<BettingCard> bettingCards = new ArrayList<>();
    /**
     * The current status of the player.
     * playing: The player is normally participating in the game.
     * currentStageForfeit: The player has been excluded from the current stage ranking.
     * gameForfeit: The player has been excluded from the entire game and is now only a passive observer.
     * disconnected: The player has lost connection to the game and is no longer participating.
     * (Required)
     */
    @SerializedName("state")
    @Expose
    @NotNull
    private Player.State state;

    /**
     * Builder player . player builder base.
     *
     * @return the player . player builder base
     */
    public static Player.PlayerBuilderBase builder() {
        return new Player.PlayerBuilder();
    }

    /**
     * The unique Id of the player. This is equal to the clientId of the client.
     * (Required)
     *
     * @return the player id
     */
    public Integer getPlayerId() {
        return playerId;
    }

    /**
     * The name of the player
     * (Required)
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * The number of coins the player has
     * (Required)
     *
     * @return the money
     */
    public Integer getMoney() {
        return money;
    }

    /**
     * A list of betting cards the player has
     * (Required)
     *
     * @return the betting cards
     */
    public List<BettingCard> getBettingCards() {
        return bettingCards;
    }

    /**
     * The current status of the player.
     * playing: The player is normally participating in the game.
     * currentStageForfeit: The player has been excluded from the current stage ranking.
     * gameForfeit: The player has been excluded from the entire game and is now only a passive observer.
     * disconnected: The player has lost connection to the game and is no longer participating.
     * (Required)
     *
     * @return the state
     */
    public Player.State getState() {
        return state;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Player.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("playerId");
        sb.append('=');
        sb.append(((this.playerId == null) ? "<null>" : this.playerId));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null) ? "<null>" : this.name));
        sb.append(',');
        sb.append("money");
        sb.append('=');
        sb.append(((this.money == null) ? "<null>" : this.money));
        sb.append(',');
        sb.append("bettingCards");
        sb.append('=');
        sb.append(((this.bettingCards == null) ? "<null>" : this.bettingCards));
        sb.append(',');
        sb.append("state");
        sb.append('=');
        sb.append(((this.state == null) ? "<null>" : this.state));
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
        result = ((result * 31) + ((this.name == null) ? 0 : this.name.hashCode()));
        result = ((result * 31) + ((this.bettingCards == null) ? 0 : this.bettingCards.hashCode()));
        result = ((result * 31) + ((this.state == null) ? 0 : this.state.hashCode()));
        result = ((result * 31) + ((this.money == null) ? 0 : this.money.hashCode()));
        result = ((result * 31) + ((this.playerId == null) ? 0 : this.playerId.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Player) == false) {
            return false;
        }
        Player rhs = ((Player) other);
        return ((((((this.name == rhs.name) || ((this.name != null) && this.name.equals(rhs.name))) && ((this.bettingCards == rhs.bettingCards) || ((this.bettingCards != null) && this.bettingCards.equals(rhs.bettingCards)))) && ((this.state == rhs.state) || ((this.state != null) && this.state.equals(rhs.state)))) && ((this.money == rhs.money) || ((this.money != null) && this.money.equals(rhs.money)))) && ((this.playerId == rhs.playerId) || ((this.playerId != null) && this.playerId.equals(rhs.playerId))));
    }

    /**
     * The current status of the player.
     * playing: The player is normally participating in the game.
     * currentStageForfeit: The player has been excluded from the current stage ranking.
     * gameForfeit: The player has been excluded from the entire game and is now only a passive observer.
     * disconnected: The player has lost connection to the game and is no longer participating.
     */
    @Generated("jsonschema2pojo")
    public enum State {

        /**
         * Playing state.
         */
        @SerializedName("playing")
        PLAYING("playing"),
        /**
         * Current stage forfeit state.
         */
        @SerializedName("currentStageForfeit")
        CURRENT_STAGE_FORFEIT("currentStageForfeit"),
        /**
         * Game forfeit state.
         */
        @SerializedName("gameForfeit")
        GAME_FORFEIT("gameForfeit"),
        /**
         * Disconnected state.
         */
        @SerializedName("disconnected")
        DISCONNECTED("disconnected");
        private final static Map<String, Player.State> CONSTANTS = new HashMap<String, Player.State>();

        static {
            for (Player.State c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        State(String value) {
            this.value = value;
        }

        /**
         * From value player . state.
         *
         * @param value the value
         * @return the player . state
         */
        public static Player.State fromValue(String value) {
            Player.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
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

    }

    /**
     * The type Player builder.
     */
    public static class PlayerBuilder
            extends Player.PlayerBuilderBase<Player> {

        /**
         * Instantiates a new Player builder.
         */
        public PlayerBuilder() {
            super();
        }

    }

    /**
     * The type Player builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class PlayerBuilderBase<T extends Player> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Player builder base.
         */
        @SuppressWarnings("unchecked")
        public PlayerBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(Player.PlayerBuilder.class)) {
                this.instance = ((T) new Player());
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
         * With player id player . player builder base.
         *
         * @param playerId the player id
         * @return the player . player builder base
         */
        public Player.PlayerBuilderBase withPlayerId(Integer playerId) {
            ((Player) this.instance).playerId = playerId;
            return this;
        }

        /**
         * With name player . player builder base.
         *
         * @param name the name
         * @return the player . player builder base
         */
        public Player.PlayerBuilderBase withName(String name) {
            ((Player) this.instance).name = name;
            return this;
        }

        /**
         * With money player . player builder base.
         *
         * @param money the money
         * @return the player . player builder base
         */
        public Player.PlayerBuilderBase withMoney(Integer money) {
            ((Player) this.instance).money = money;
            return this;
        }

        /**
         * With betting cards player . player builder base.
         *
         * @param bettingCards the betting cards
         * @return the player . player builder base
         */
        public Player.PlayerBuilderBase withBettingCards(List<BettingCard> bettingCards) {
            ((Player) this.instance).bettingCards = bettingCards;
            return this;
        }

        /**
         * With state player . player builder base.
         *
         * @param state the state
         * @return the player . player builder base
         */
        public Player.PlayerBuilderBase withState(Player.State state) {
            ((Player) this.instance).state = state;
            return this;
        }

    }

}
