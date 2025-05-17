package com.oasisstudios.camelupengine.dto;

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

    public static class RequestLobbyListBuilder
        extends RequestLobbyList.RequestLobbyListBuilderBase<RequestLobbyList> {


        public RequestLobbyListBuilder() {
            super();
        }

    }

    public static abstract class RequestLobbyListBuilderBase<T extends RequestLobbyList> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public RequestLobbyListBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(RequestLobbyList.RequestLobbyListBuilder.class)) {
                this.instance = ((T) new RequestLobbyList());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

    }

}
