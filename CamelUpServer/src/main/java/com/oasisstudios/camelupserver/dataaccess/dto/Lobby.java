package com.oasisstudios.camelupserver.dataaccess.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;

/**
 * Game
 */
@Generated("jsonschema2pojo")
@Data
public class Lobby {

    public Lobby(Integer lobbyId, String name, GameState gameState, List<Integer> observerIds) {
        this.lobbyId = lobbyId;
        this.name = name;
        this.gameState = gameState;
        this.observerIds = observerIds;
    }
    public Lobby(){}
    /**
     * Unique identifier for a lobby.
     * (Required)
     */
    @SerializedName("lobbyId")
    @Expose
    @NotNull
    private Integer lobbyId;
    /**
     * Display name of the lobby.
     * (Required)
     */
    @SerializedName("name")
    @Expose
    @NotNull
    private String name;
    /**
     * gameState
     * <p>
     * The current state of the game, which is only sent by the server during an active game when updates occur.
     * (Required)
     */
    @SerializedName("gameState")
    @Expose
    @Valid
    @NotNull
    private GameState gameState;
    /**
     * A list of observer IDs who have joined the lobby. These correspond to the client IDs of the respective client.
     */
    @SerializedName("observerIds")
    @Expose
    @Valid
    private List<Integer> observerIds = new ArrayList<Integer>();

    public static Lobby.LobbyBuilderBase builder() {
        return new Lobby.LobbyBuilder();
    }

    /**
     * Unique identifier for a lobby.
     * (Required)
     */
    public Integer getLobbyId() {
        return lobbyId;
    }

    /**
     * Display name of the lobby.
     * (Required)
     */
    public String getName() {
        return name;
    }

    /**
     * gameState
     * <p>
     * The current state of the game, which is only sent by the server during an active game when updates occur.
     * (Required)
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * A list of observer IDs who have joined the lobby. These correspond to the client IDs of the respective client.
     */
    public List<Integer> getObserverIds() {
        return observerIds;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Lobby.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("lobbyId");
        sb.append('=');
        sb.append(((this.lobbyId == null) ? "<null>" : this.lobbyId));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null) ? "<null>" : this.name));
        sb.append(',');
        sb.append("gameState");
        sb.append('=');
        sb.append(((this.gameState == null) ? "<null>" : this.gameState));
        sb.append(',');
        sb.append("observerIds");
        sb.append('=');
        sb.append(((this.observerIds == null) ? "<null>" : this.observerIds));
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
        result = ((result * 31) + ((this.observerIds == null) ? 0 : this.observerIds.hashCode()));
        result = ((result * 31) + ((this.gameState == null) ? 0 : this.gameState.hashCode()));
        result = ((result * 31) + ((this.lobbyId == null) ? 0 : this.lobbyId.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Lobby) == false) {
            return false;
        }
        Lobby rhs = ((Lobby) other);
        return (((((((this.name == rhs.name) || ((this.name != null) && this.name.equals(rhs.name))) && ((this.observerIds == rhs.observerIds) || ((this.observerIds != null) && this.observerIds.equals(rhs.observerIds)))) && ((this.gameState == rhs.gameState) || ((this.gameState != null) && this.gameState.equals(rhs.gameState)))) && ((this.lobbyId == rhs.lobbyId) || ((this.lobbyId != null) && this.lobbyId.equals(rhs.lobbyId))))));
    }

    public static class LobbyBuilder
        extends Lobby.LobbyBuilderBase<Lobby> {


        public LobbyBuilder() {
            super();
        }

    }

    public static abstract class LobbyBuilderBase<T extends Lobby> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public LobbyBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(Lobby.LobbyBuilder.class)) {
                this.instance = ((T) new Lobby());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public Lobby.LobbyBuilderBase withLobbyId(Integer lobbyId) {
            ((Lobby) this.instance).lobbyId = lobbyId;
            return this;
        }

        public Lobby.LobbyBuilderBase withName(String name) {
            ((Lobby) this.instance).name = name;
            return this;
        }

        public Lobby.LobbyBuilderBase withGameState(GameState gameState) {
            ((Lobby) this.instance).gameState = gameState;
            return this;
        }

        public Lobby.LobbyBuilderBase withObserverIds(List<Integer> observerIds) {
            ((Lobby) this.instance).observerIds = observerIds;
            return this;
        }

    }

}
