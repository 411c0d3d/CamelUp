package com.oasisstudios.camelupserver.dataaccess.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.Map;


/**
 * Packet
 * <p>
 * The basic structure of a packet through which all communication runs. It includes all other packet types and is therefore always used whenever any kind of communication takes place.
 */
@Generated("jsonschema2pojo")
@Data
public class Packet {
    /**
     * Instantiates a new Packet.
     *
     * @param type    the type
     * @param content the content
     */
    public Packet(Type type, Object content) {
        this.type = type;
        this.content = content;
    }

    /**
     * Instantiates a new Packet.
     */
    public Packet(){}
    /**
     * The type of the packet.
     * (Required)
     */
    @SerializedName("type")
    @Expose
    @NotNull
    private Packet.Type type;
    /**
     * The content of the packet. This must always match the type of the packet.
     * (Required)
     */
    @SerializedName("content")
    @Expose
    @Valid
    @NotNull
    private Object content;

    /**
     * Builder packet . packet builder base.
     *
     * @return the packet . packet builder base
     */
    public static Packet.PacketBuilderBase builder() {
        return new Packet.PacketBuilder();
    }

    /**
     * The type of the packet.
     * (Required)
     *
     * @return the type
     */
    public Packet.Type getType() {
        return type;
    }

    /**
     * The content of the packet. This must always match the type of the packet.
     * (Required)
     *
     * @return the content
     */
    public Object getContent() {
        return content;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Packet.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null) ? "<null>" : this.type));
        sb.append(',');
        sb.append("content");
        sb.append('=');
        sb.append(((this.content == null) ? "<null>" : this.content));
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
        result = ((result * 31) + ((this.type == null) ? 0 : this.type.hashCode()));
        result = ((result * 31) + ((this.content == null) ? 0 : this.content.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Packet) == false) {
            return false;
        }
        Packet rhs = ((Packet) other);
        return (((this.type == rhs.type) || ((this.type != null) && this.type.equals(rhs.type))) && ((this.content == rhs.content) || ((this.content != null) && this.content.equals(rhs.content))));
    }

    /**
     * The type of the packet.
     */
    @Generated("jsonschema2pojo")
    public enum Type {

        /**
         * Client ack type.
         */
        @SerializedName("clientAck")
        CLIENT_ACK("clientAck"),
        /**
         * Player registration type.
         */
        @SerializedName("playerRegistration")
        PLAYER_REGISTRATION("playerRegistration"),
        /**
         * Final bet type.
         */
        @SerializedName("finalBet")
        FINAL_BET("finalBet"),
        /**
         * Game state type.
         */
        @SerializedName("gameState")
        GAME_STATE("gameState"),
        /**
         * Join lobby type.
         */
        @SerializedName("joinLobby")
        JOIN_LOBBY("joinLobby"),
        /**
         * Lobby list type.
         */
        @SerializedName("lobbyList")
        LOBBY_LIST("lobbyList"),
        /**
         * Move visualized type.
         */
        @SerializedName("moveVisualized")
        MOVE_VISUALIZED("moveVisualized"),
        /**
         * Place player card type.
         */
        @SerializedName("placePlayerCard")
        PLACE_PLAYER_CARD("placePlayerCard"),
        /**
         * Recent games type.
         */
        @SerializedName("recentGames")
        RECENT_GAMES("recentGames"),
        /**
         * Request lobby list type.
         */
        @SerializedName("requestLobbyList")
        REQUEST_LOBBY_LIST("requestLobbyList"),
        /**
         * Request recent games type.
         */
        @SerializedName("requestRecentGames")
        REQUEST_RECENT_GAMES("requestRecentGames"),
        /**
         * Roll dice type.
         */
        @SerializedName("rollDice")
        ROLL_DICE("rollDice"),
        /**
         * Stage bet type.
         */
        @SerializedName("stageBet")
        STAGE_BET("stageBet"),
        /**
         * Success feedback type.
         */
        @SerializedName("successFeedback")
        SUCCESS_FEEDBACK("successFeedback"),
        /**
         * Game end type.
         */
        @SerializedName("gameEnd")
        GAME_END("gameEnd"),

        @SerializedName("startGame")
        START_GAME("startGame"),

        @SerializedName("pauseGame")
        PAUSE_GAME("pauseGame"),

        @SerializedName("resumeGame")
        RESUME_GAME("resumeGame"),

        @SerializedName("createLobby")
        CREATE_LOBBY("createLobby"),
        
        @SerializedName("adminChannel")
        ADMIN_CHANNEL("adminChannel"),
        
        @SerializedName("pendingPlayers")
        PENDING_PLAYERS("pendingPlayers");

        private final static Map<String, Packet.Type> CONSTANTS = new HashMap<String, Packet.Type>();

        static {
            for (Packet.Type c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        Type(String value) {
            this.value = value;
        }

        /**
         * From value packet . type.
         *
         * @param value the value
         * @return the packet . type
         */
        public static Packet.Type fromValue(String value) {
            Packet.Type constant = CONSTANTS.get(value);
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
     * The type Packet builder.
     */
    public static class PacketBuilder
            extends Packet.PacketBuilderBase<Packet> {


        /**
         * Instantiates a new Packet builder.
         */
        public PacketBuilder() {
            super();
        }

    }

    /**
     * The type Packet builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class PacketBuilderBase<T extends Packet> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Packet builder base.
         */
        @SuppressWarnings("unchecked")
        public PacketBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(Packet.PacketBuilder.class)) {
                this.instance = ((T) new Packet());
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
         * With type packet . packet builder base.
         *
         * @param type the type
         * @return the packet . packet builder base
         */
        public Packet.PacketBuilderBase withType(Packet.Type type) {
            ((Packet) this.instance).type = type;
            return this;
        }

        /**
         * With content packet . packet builder base.
         *
         * @param content the content
         * @return the packet . packet builder base
         */
        public Packet.PacketBuilderBase withContent(Object content) {
            ((Packet) this.instance).content = content;
            return this;
        }

    }

}
