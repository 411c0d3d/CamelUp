package com.oasisstudios.camelupadmin.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;


/**
 * lobbyList
 * <p>
 * The package sent in response to 'requestLobbyList'. The package contains a list of lobby objects, which include information about the joined players and observers, as well as the game status.
 */
@Generated("jsonschema2pojo")
@Data
public class LobbyList {

    /**
     * A list of lobby objects that can be joined as a player or observer.
     * (Required)
     */
    @SerializedName("lobbies")
    @Expose
    @Size(min = 0)
    @Valid
    @NotNull
    private List<Lobby> lobbies = new ArrayList<Lobby>();


    /**
     * A list of lobby objects that can be joined as a player or observer.
     * (Required)
     */
    public List<Lobby> getLobbies() {
        return lobbies;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(LobbyList.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("lobbies");
        sb.append('=');
        sb.append(((this.lobbies == null) ? "<null>" : this.lobbies));
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
        result = ((result * 31) + ((this.lobbies == null) ? 0 : this.lobbies.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LobbyList) == false) {
            return false;
        }
        LobbyList rhs = ((LobbyList) other);
        return ((this.lobbies == rhs.lobbies) || ((this.lobbies != null) && this.lobbies.equals(rhs.lobbies)));
    }
    public static LobbyList.LobbyListBuilderBase builder() {
        return new LobbyList.LobbyListBuilder();
    }

    public static class LobbyListBuilder
        extends LobbyList.LobbyListBuilderBase<LobbyList> {


        public LobbyListBuilder() {
            super();
        }

    }

    public static abstract class LobbyListBuilderBase<T extends LobbyList> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public LobbyListBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(LobbyList.LobbyListBuilder.class)) {
                this.instance = ((T) new LobbyList());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public LobbyList.LobbyListBuilderBase withLobbies(List<Lobby> lobbies) {
            ((LobbyList) this.instance).lobbies = lobbies;
            return this;
        }

    }

}
