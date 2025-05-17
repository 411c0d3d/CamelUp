package com.oasisstudios.camelupserver.dataaccess.dto;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * recentGamesReq
 * <p>
 * Requests the recentGames package from the server. The server will send the 'recentGames' package in response.
 */
@Generated("jsonschema2pojo")
@Data
public class RequestRecentGames {

    /**
     * Instantiates a new Request recent games.
     *
     * @param numGames the num games
     */
    public RequestRecentGames(Integer numGames) {
        this.numGames = numGames;
    }

    /**
     * Instantiates a new Request recent games.
     */
    public RequestRecentGames(){}
    /**
     * The number of games the client wants to receive from the server. If there are fewer games than requested, the server will send all available games.
     * (Required)
     */
    @SerializedName("numGames")
    @Expose
    @DecimalMin("1")
    @NotNull
    private Integer numGames;

    /**
     * Builder request recent games . request recent games builder base.
     *
     * @return the request recent games . request recent games builder base
     */
    public static RequestRecentGames.RequestRecentGamesBuilderBase builder() {
        return new RequestRecentGames.RequestRecentGamesBuilder();
    }

    /**
     * The number of games the client wants to receive from the server. If there are fewer games than requested, the server will send all available games.
     * (Required)
     *
     * @return the num games
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

    /**
     * The type Request recent games builder.
     */
    public static class RequestRecentGamesBuilder
        extends RequestRecentGames.RequestRecentGamesBuilderBase<RequestRecentGames> {


        /**
         * Instantiates a new Request recent games builder.
         */
        public RequestRecentGamesBuilder() {
            super();
        }

    }

    /**
     * The type Request recent games builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class RequestRecentGamesBuilderBase<T extends RequestRecentGames> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Request recent games builder base.
         */
        @SuppressWarnings("unchecked")
        public RequestRecentGamesBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(RequestRecentGames.RequestRecentGamesBuilder.class)) {
                this.instance = ((T) new RequestRecentGames());
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
         * With num games request recent games . request recent games builder base.
         *
         * @param numGames the num games
         * @return the request recent games . request recent games builder base
         */
        public RequestRecentGames.RequestRecentGamesBuilderBase withNumGames(Integer numGames) {
            ((RequestRecentGames) this.instance).numGames = numGames;
            return this;
        }

    }

}
