package com.oasisstudios.camelupserver.dataaccess.dto;

import lombok.Data;

import javax.annotation.processing.Generated;


/**
 * requestLobbyList
 * <p>
 * Query packet to retrieve the list of all available lobbies. This is sent from the client to the server.
 */
@Generated("jsonschema2pojo")
@Data
public class RequestLobbyList {
    /**
     * Instantiates a new Request lobby list.
     */
    public RequestLobbyList() {}

    /**
     * Builder request lobby list . request lobby list builder base.
     *
     * @return the request lobby list . request lobby list builder base
     */
    public static RequestLobbyList.RequestLobbyListBuilderBase builder() {
        return new RequestLobbyList.RequestLobbyListBuilder();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RequestLobbyList.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RequestLobbyList) == false) {
            return false;
        }
        RequestLobbyList rhs = ((RequestLobbyList) other);
        return true;
    }

    /**
     * The type Request lobby list builder.
     */
    public static class RequestLobbyListBuilder
        extends RequestLobbyList.RequestLobbyListBuilderBase<RequestLobbyList> {


        /**
         * Instantiates a new Request lobby list builder.
         */
        public RequestLobbyListBuilder() {
            super();
        }

    }

    /**
     * The type Request lobby list builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class RequestLobbyListBuilderBase<T extends RequestLobbyList> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Request lobby list builder base.
         */
        @SuppressWarnings("unchecked")
        public RequestLobbyListBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(RequestLobbyList.RequestLobbyListBuilder.class)) {
                this.instance = ((T) new RequestLobbyList());
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

    }

}
