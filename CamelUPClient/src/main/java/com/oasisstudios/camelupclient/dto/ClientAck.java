package com.oasisstudios.camelupclient.dto;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * clientAck
 * <p>
 * After the client has initially connected to the server, the server sends this packet to inform the client of its clientID.
 */
@Generated("jsonschema2pojo")
@Data
public class ClientAck {

    /**
     * The clientId assigned to the client. These start at 0 and are numbered sequentially.
     * (Required)
     */
    @SerializedName("clientId")
    @Expose
    @DecimalMin("0")
    @NotNull
    private Integer clientId;

    public static ClientAck.ClientAckBuilderBase builder() {
        return new ClientAck.ClientAckBuilder();
    }

    /**
     * The clientId assigned to the client. These start at 0 and are numbered sequentially.
     * (Required)
     */
    public Integer getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ClientAck.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("clientId");
        sb.append('=');
        sb.append(((this.clientId == null) ? "<null>" : this.clientId));
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
        result = ((result * 31) + ((this.clientId == null) ? 0 : this.clientId.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ClientAck) == false) {
            return false;
        }
        ClientAck rhs = ((ClientAck) other);
        return ((this.clientId == rhs.clientId) || ((this.clientId != null) && this.clientId.equals(rhs.clientId)));
    }

    public static class ClientAckBuilder
        extends ClientAck.ClientAckBuilderBase<ClientAck> {


        public ClientAckBuilder() {
            super();
        }

    }

    public static abstract class ClientAckBuilderBase<T extends ClientAck> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public ClientAckBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(ClientAck.ClientAckBuilder.class)) {
                this.instance = ((T) new ClientAck());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public ClientAck.ClientAckBuilderBase withClientId(Integer clientId) {
            ((ClientAck) this.instance).clientId = clientId;
            return this;
        }

    }

}
