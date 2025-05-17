package com.oasisstudios.camelupadmin.dto;

import lombok.Data;

import javax.annotation.processing.Generated;
import java.io.Serializable;

@Generated("jsonschema2pojo")
@Data
public class AdminChannel implements Serializable {
    /**
     * Instantiates a new AdminChannel.
     */
    public AdminChannel() {
    }
    /**
     * Builder admin channel . admin channel builder base.
     *
     * @return the admin channel . admin channel builder base
     */
    public static AdminChannel.AdminChannelBuilderBase builder() {
        return new AdminChannel.AdminChannelBuilder();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(AdminChannel.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        if ((other instanceof AdminChannel) == false) {
            return false;
        }
        AdminChannel rhs = ((AdminChannel) other);
        return true;
    }

    /**
     * The type admin channel builder.
     */
    public static class AdminChannelBuilder
            extends AdminChannel.AdminChannelBuilderBase<AdminChannel> {


        /**
         * Instantiates a new admin channel builder.
         */
        public AdminChannelBuilder() {
            super();
        }

    }

    /**
     * The type admin channel builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class AdminChannelBuilderBase<T extends AdminChannel> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new admin channel builder base.
         */
        @SuppressWarnings("unchecked")
        public AdminChannelBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(AdminChannel.AdminChannelBuilder.class)) {
                this.instance = ((T) new AdminChannel());
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