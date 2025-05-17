package com.oasisstudios.camelupengine.dto;

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

    public static Packet.PacketBuilderBase builder() {
        return new Packet.PacketBuilder();
    }

    /**
     * The type of the packet.
     * (Required)
     */
    public Packet.Type getType() {
        return type;
    }

    /**
     * The content of the packet. This must always match the type of the packet.
     * (Required)
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

        @SerializedName("clientAck")
        CLIENT_ACK("clientAck"),
        @SerializedName("playerRegistration")
        PLAYER_REGISTRATION("playerRegistration"),
        @SerializedName("finalBet")
        FINAL_BET("finalBet"),
        @SerializedName("gameState")
        GAME_STATE("gameState"),
        @SerializedName("joinLobby")
        JOIN_LOBBY("joinLobby"),
        @SerializedName("lobbyList")
        LOBBY_LIST("lobbyList"),
        @SerializedName("moveVisualized")
        MOVE_VISUALIZED("moveVisualized"),
        @SerializedName("placePlayerCard")
        PLACE_PLAYER_CARD("placePlayerCard"),
        @SerializedName("recentGames")
        RECENT_GAMES("recentGames"),
        @SerializedName("requestLobbyList")
        REQUEST_LOBBY_LIST("requestLobbyList"),
        @SerializedName("requestRecentGames")
        REQUEST_RECENT_GAMES("requestRecentGames"),
        @SerializedName("rollDice")
        ROLL_DICE("rollDice"),
        @SerializedName("stageBet")
        STAGE_BET("stageBet"),
        @SerializedName("successFeedback")
        SUCCESS_FEEDBACK("successFeedback"),
        @SerializedName("gameEnd")
        GAME_END("gameEnd"),
        @SerializedName("createLobby")
        CREATE_LOBBY("createLobby"),
        @SerializedName("registeredPlayers")
        REGISTERED_PLAYERS("registeredPlayers"),
        @SerializedName("registeredPlayerList")
        PLAYER_LIST("registeredPlayerList");
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

        public String value() {
            return this.value;
        }

    }

    public static class PacketBuilder
        extends Packet.PacketBuilderBase<Packet> {


        public PacketBuilder() {
            super();
        }

    }

    public static abstract class PacketBuilderBase<T extends Packet> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public PacketBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(Packet.PacketBuilder.class)) {
                this.instance = ((T) new Packet());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public Packet.PacketBuilderBase withType(Packet.Type type) {
            ((Packet) this.instance).type = type;
            return this;
        }

        public Packet.PacketBuilderBase withContent(Object content) {
            ((Packet) this.instance).content = content;
            return this;
        }

    }

}
