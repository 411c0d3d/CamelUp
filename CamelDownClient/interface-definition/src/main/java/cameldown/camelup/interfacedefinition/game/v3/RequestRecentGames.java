
package cameldown.camelup.interfacedefinition.game.v3;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;


/**
 * recentGamesReq
 * <p>
 * Requests the recentGames package from the server. The server will send the 'recentGames' package in response.
 */
@Generated("jsonschema2pojo")
public class RequestRecentGames {

    /**
     * The number of games the client wants to receive from the server. If there are fewer games than requested, the server will send all available games.
     * (Required)
     */
    @SerializedName("numGames")
    @Expose
    @DecimalMin("1")
    @NotNull
    private Integer numGames;

    public static RequestRecentGames.RequestRecentGamesBuilderBase builder() {
        return new RequestRecentGames.RequestRecentGamesBuilder();
    }

    /**
     * The number of games the client wants to receive from the server. If there are fewer games than requested, the server will send all available games.
     * (Required)
     */
    public Integer getNumGames() {
        return numGames;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RequestRecentGames.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("numGames");
        sb.append('=');
        sb.append(((this.numGames == null) ? "<null>" : this.numGames));
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
        result = ((result * 31) + ((this.numGames == null) ? 0 : this.numGames.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RequestRecentGames) == false) {
            return false;
        }
        RequestRecentGames rhs = ((RequestRecentGames) other);
        return ((this.numGames == rhs.numGames) || ((this.numGames != null) && this.numGames.equals(rhs.numGames)));
    }

    public static class RequestRecentGamesBuilder
        extends RequestRecentGames.RequestRecentGamesBuilderBase<RequestRecentGames> {


        public RequestRecentGamesBuilder() {
            super();
        }

    }

    public static abstract class RequestRecentGamesBuilderBase<T extends RequestRecentGames> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public RequestRecentGamesBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(RequestRecentGames.RequestRecentGamesBuilder.class)) {
                this.instance = ((T) new RequestRecentGames());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public RequestRecentGames.RequestRecentGamesBuilderBase withNumGames(Integer numGames) {
            ((RequestRecentGames) this.instance).numGames = numGames;
            return this;
        }

    }

}
