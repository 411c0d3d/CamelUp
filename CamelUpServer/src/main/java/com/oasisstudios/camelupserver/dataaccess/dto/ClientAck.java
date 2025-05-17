package com.oasisstudios.camelupserver.dataaccess.dto;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


/**
 * clientAck
 * <p>
 * After the client has initially connected to the server, the server sends this packet to inform the client of its clientID.
 */
@Generated("jsonschema2pojo")
@Data
public class ClientAck implements Serializable {
    
    /**
     * Instantiates a new Client ack.
     *
     * @param clientId the client id
     */
    public ClientAck(Integer clientId) {
        this.clientId = clientId;
        this.clientName = "";
    }
    public ClientAck() {}

    /**
     * The clientId assigned to the client. These start at 0 and are numbered sequentially.
     * (Required)
     */
    
    @SerializedName("clientId")
    @Expose
    @DecimalMin("0")
    @NotNull
    private Integer clientId;

    private transient String clientName;

    /**
     * Builder client ack . client ack builder base.
     *
     * @return the client ack . client ack builder base
     */
    public static ClientAck.ClientAckBuilderBase builder() {
        return new ClientAck.ClientAckBuilder();
    }

    /**
     * The clientId assigned to the client. These start at 0 and are numbered sequentially.
     * (Required)
     *
     * @return the client id
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

    /**
     * The type Client ack builder.
     */
    public static class ClientAckBuilder
        extends ClientAck.ClientAckBuilderBase<ClientAck> {


        /**
         * Instantiates a new Client ack builder.
         */
        public ClientAckBuilder() {
            super();
        }

    }

    /**
     * The type Client ack builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class ClientAckBuilderBase<T extends ClientAck> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Client ack builder base.
         */
        @SuppressWarnings("unchecked")
        public ClientAckBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(ClientAck.ClientAckBuilder.class)) {
                this.instance = ((T) new ClientAck());
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
         * With client id client ack . client ack builder base.
         *
         * @param clientId the client id
         * @return the client ack . client ack builder base
         */
        public ClientAck.ClientAckBuilderBase withClientId(Integer clientId) {
            ((ClientAck) this.instance).clientId = clientId;
            return this;
        }

    }

}
