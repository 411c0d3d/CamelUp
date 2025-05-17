package com.oasisstudios.camelupserver.dataaccess.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import javax.annotation.processing.Generated;
import java.io.Serializable;

/**
 * CreateLobby
 * <p>
 * Used for creating a new lobby. Contains the lobby details.
 */
@Generated("jsonschema2pojo")
@Data
public class CreateLobby implements Serializable {

    /**
     * The lobby details for creating a new lobby.
     * (Required)
     */
    @SerializedName("createLobby")
    @Expose
    @NotNull
    private Lobby lobby;

    /**
     * Constructor for CreateLobby.
     *
     * @param lobby the lobby details
     */
    public CreateLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public static CreateLobby.CreateLobbyBuilderBase builder() {
        return new CreateLobby.CreateLobbyBuilder();
    }

    /**
     * Returns the lobby details.
     *
     * @return the lobby
     */
    public Lobby getLobby() {
        return lobby;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(CreateLobby.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("lobby");
        sb.append('=');
        sb.append(((this.lobby == null) ? "<null>" : this.lobby));
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
        result = ((result * 31) + ((this.lobby == null) ? 0 : this.lobby.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CreateLobby) == false) {
            return false;
        }
        CreateLobby rhs = ((CreateLobby) other);
        return ((this.lobby == rhs.lobby) || ((this.lobby != null) && this.lobby.equals(rhs.lobby)));
    }

    public static class CreateLobbyBuilder extends CreateLobby.CreateLobbyBuilderBase<CreateLobby> {
        public CreateLobbyBuilder() {
            super();
        }
    }

    public static abstract class CreateLobbyBuilderBase<T extends CreateLobby> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public CreateLobbyBuilderBase() {
            if (this.getClass().equals(CreateLobby.CreateLobbyBuilder.class)) {
                this.instance = ((T) new CreateLobby(null));
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public CreateLobby.CreateLobbyBuilderBase withLobby(Lobby lobby) {
            ((CreateLobby) this.instance).lobby = lobby;
            return this;
        }
    }
}
