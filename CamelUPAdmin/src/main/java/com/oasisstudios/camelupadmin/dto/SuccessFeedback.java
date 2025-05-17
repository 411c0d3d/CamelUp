package com.oasisstudios.camelupadmin.dto;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * successFeedback
 * <p>
 * Structure of a success/error message. This is sent by the client/server to indicate the successful completion of the previous request.
 */
@Generated("jsonschema2pojo")
@Data
public class SuccessFeedback {

    /**
     * Indicates whether the request was successful or failed. The field is true if the request was successful, and false otherwise.
     * (Required)
     */
    @SerializedName("success")
    @Expose
    @NotNull
    private Boolean success;
    /**
     * Indicates the type of the previously sent request. For example, if the playerRegistration request was sent, this field will contain 'playerRegistration'.
     * (Required)
     */
    @SerializedName("request")
    @Expose
    @NotNull
    private Packet request;
    /**
     * This field is optional. In case of a request failure, this field can contain details about what exactly failed.
     */
    @SerializedName("error")
    @Expose
    private String error;

    public static SuccessFeedback.SuccessFeedbackBuilderBase builder() {
        return new SuccessFeedback.SuccessFeedbackBuilder();
    }

    /**
     * Indicates whether the request was successful or failed. The field is true if the request was successful, and false otherwise.
     * (Required)
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * Indicates the type of the previously sent request. For example, if the playerRegistration request was sent, this field will contain 'playerRegistration'.
     * (Required)
     */
    public Packet getRequest() {
        return request;
    }

    /**
     * This field is optional. In case of a request failure, this field can contain details about what exactly failed.
     */
    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SuccessFeedback.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("success");
        sb.append('=');
        sb.append(((this.success == null) ? "<null>" : this.success));
        sb.append(',');
        sb.append("request");
        sb.append('=');
        sb.append(((this.request == null) ? "<null>" : this.request));
        sb.append(',');
        sb.append("error");
        sb.append('=');
        sb.append(((this.error == null) ? "<null>" : this.error));
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
        result = ((result * 31) + ((this.error == null) ? 0 : this.error.hashCode()));
        result = ((result * 31) + ((this.request == null) ? 0 : this.request.hashCode()));
        result = ((result * 31) + ((this.success == null) ? 0 : this.success.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SuccessFeedback) == false) {
            return false;
        }
        SuccessFeedback rhs = ((SuccessFeedback) other);
        return ((((this.error == rhs.error) || ((this.error != null) && this.error.equals(rhs.error))) && ((this.request == rhs.request) || ((this.request != null) && this.request.equals(rhs.request)))) && ((this.success == rhs.success) || ((this.success != null) && this.success.equals(rhs.success))));
    }

    public static class SuccessFeedbackBuilder
        extends SuccessFeedback.SuccessFeedbackBuilderBase<SuccessFeedback> {


        public SuccessFeedbackBuilder() {
            super();
        }

    }

    public static abstract class SuccessFeedbackBuilderBase<T extends SuccessFeedback> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public SuccessFeedbackBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(SuccessFeedback.SuccessFeedbackBuilder.class)) {
                this.instance = ((T) new SuccessFeedback());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public SuccessFeedback.SuccessFeedbackBuilderBase withSuccess(Boolean success) {
            ((SuccessFeedback) this.instance).success = success;
            return this;
        }

        public SuccessFeedback.SuccessFeedbackBuilderBase withRequest(Packet request) {
            ((SuccessFeedback) this.instance).request = request;
            return this;
        }

        public SuccessFeedback.SuccessFeedbackBuilderBase withError(String error) {
            ((SuccessFeedback) this.instance).error = error;
            return this;
        }

    }

}
