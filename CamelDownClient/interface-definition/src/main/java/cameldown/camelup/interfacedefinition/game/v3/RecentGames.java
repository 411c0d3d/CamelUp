
package cameldown.camelup.interfacedefinition.game.v3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;


/**
 * recentGames
 * <p>
 * Contains a list of games that have ended. This package is sent as a response to 'requestRecentGames' from the server.
 */
@Generated("jsonschema2pojo")
public class RecentGames {

    /**
     * A list of games that have ended.
     * (Required)
     */
    @SerializedName("lobbies")
    @Expose
    @Valid
    @NotNull
    private List<RecentGame> lobbies = new ArrayList<RecentGame>();

    public static RecentGames.RecentGamesBuilderBase builder() {
        return new RecentGames.RecentGamesBuilder();
    }

    /**
     * A list of games that have ended.
     * (Required)
     */
    public List<RecentGame> getLobbies() {
        return lobbies;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RecentGames.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        if ((other instanceof RecentGames) == false) {
            return false;
        }
        RecentGames rhs = ((RecentGames) other);
        return ((this.lobbies == rhs.lobbies) || ((this.lobbies != null) && this.lobbies.equals(rhs.lobbies)));
    }

    public static class RecentGamesBuilder
        extends RecentGames.RecentGamesBuilderBase<RecentGames> {


        public RecentGamesBuilder() {
            super();
        }

    }

    public static abstract class RecentGamesBuilderBase<T extends RecentGames> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public RecentGamesBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(RecentGames.RecentGamesBuilder.class)) {
                this.instance = ((T) new RecentGames());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public RecentGames.RecentGamesBuilderBase withRecentGames(List<RecentGame> recentGames) {
            ((RecentGames) this.instance).lobbies = recentGames;
            return this;
        }

    }

}
