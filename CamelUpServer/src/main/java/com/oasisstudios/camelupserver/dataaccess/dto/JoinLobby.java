package com.oasisstudios.camelupserver.dataaccess.dto;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * joinLobby
 * <p>
 * A package to join a specific lobby. The client can send this package to the server to join a lobby with a specific ID as a player or observer. The client then receives a successResponse indicating whether the join was successful. In case of an error, the response contains an appropriate error message. Alternatively, the joinLobby package can also be sent from the server to the client when the player has been assigned to a lobby server-side. In this case, the client can assume that it is now in the lobby with the lobbyID transmitted in the joinLobby package.
 */
@Generated("jsonschema2pojo")
@Data
public class JoinLobby {
    /**
     * Instantiates a new Join lobby.
     *
     * @param lobbyId      the lobby id
     * @param joinAsPlayer the join as player
     */
    public JoinLobby(Integer lobbyId, Boolean joinAsPlayer) {
        this.lobbyId = lobbyId;
        this.joinAsPlayer = joinAsPlayer;
    }
    
    /**
     * Instantiates a new Join lobby.
     */
    public JoinLobby(){}
    /**
     * The ID of the lobby to join. If this has the value -1, the package is interpreted as 'leaveLobby'.
     * (Required)
     */
    @SerializedName("lobbyId")
    @Expose
    @DecimalMin("-1")
    @NotNull
    private Integer lobbyId;
    /**
     * Indicates whether to join as a player (true) or observer (false)",
     * (Required)
     */
    @SerializedName("joinAsPlayer")
    @Expose
    @NotNull
    private Boolean joinAsPlayer;

    public static JoinLobby.JoinLobbyBuilderBase builder() {
        return new JoinLobby.JoinLobbyBuilder();
    }

    /**
     * The ID of the lobby to join. If this has the value -1, the package is interpreted as 'leaveLobby'.
     * (Required)
     *
     * @return the lobby id
     */
    public Integer getLobbyId() {
        return lobbyId;
    }

    /**
     * Indicates whether to join as a player (true) or observer (false)
     * (Required)
     *
     * @return the join as player
     */
    public Boolean getJoinAsPlayer() {
        return joinAsPlayer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(JoinLobby.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("lobbyId");
        sb.append('=');
        sb.append(((this.lobbyId == null) ? "<null>" : this.lobbyId));
        sb.append(',');
        sb.append("joinAsPlayer");
        sb.append('=');
        sb.append(((this.joinAsPlayer == null) ? "<null>" : this.joinAsPlayer));
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
        result = ((result * 31) + ((this.joinAsPlayer == null) ? 0 : this.joinAsPlayer.hashCode()));
        result = ((result * 31) + ((this.lobbyId == null) ? 0 : this.lobbyId.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JoinLobby) == false) {
            return false;
        }
        JoinLobby rhs = ((JoinLobby) other);
        return (((this.joinAsPlayer == rhs.joinAsPlayer) || ((this.joinAsPlayer != null) && this.joinAsPlayer.equals(rhs.joinAsPlayer))) && ((this.lobbyId == rhs.lobbyId) || ((this.lobbyId != null) && this.lobbyId.equals(rhs.lobbyId))));
    }

    /**
     * The type Join lobby builder.
     */
    public static class JoinLobbyBuilder
        extends JoinLobby.JoinLobbyBuilderBase<JoinLobby> {


        /**
         * Instantiates a new Join lobby builder.
         */
        public JoinLobbyBuilder() {
            super();
        }

    }

    /**
     * The type Join lobby builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class JoinLobbyBuilderBase<T extends JoinLobby> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Join lobby builder base.
         */
        @SuppressWarnings("unchecked")
        public JoinLobbyBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(JoinLobby.JoinLobbyBuilder.class)) {
                this.instance = ((T) new JoinLobby());
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
         * With lobby id join lobby . join lobby builder base.
         *
         * @param lobbyId the lobby id
         * @return the join lobby . join lobby builder base
         */
        public JoinLobby.JoinLobbyBuilderBase withLobbyId(Integer lobbyId) {
            ((JoinLobby) this.instance).lobbyId = lobbyId;
            return this;
        }

        /**
         * With join as player join lobby . join lobby builder base.
         *
         * @param joinAsPlayer the join as player
         * @return the join lobby . join lobby builder base
         */
        public JoinLobby.JoinLobbyBuilderBase withJoinAsPlayer(Boolean joinAsPlayer) {
            ((JoinLobby) this.instance).joinAsPlayer = joinAsPlayer;
            return this;
        }
    }
}
