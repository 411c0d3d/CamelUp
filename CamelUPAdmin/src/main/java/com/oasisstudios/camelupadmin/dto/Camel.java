package com.oasisstudios.camelupadmin.dto;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


/**
 * camel
 * <p>
 * A specific camel
 */
@Generated("jsonschema2pojo")
@Data
public class Camel {

    /**
     * The unique ID of the camel. The IDs -1 and -2 are reserved for the two camels running backwards. All other camels start at 0 and are numbered consecutively.
     * (Required)
     */
    @SerializedName("id")
    @Expose
    @DecimalMin("-2")
    @DecimalMax("32767")
    @NotNull
    private Integer id;
    /**
     * The color of the camel in the form of a hexadecimal color code
     * (Required)
     */
    @SerializedName("color")
    @Expose
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
    @NotNull
    private String color;

    public static Camel.CamelBuilderBase builder() {
        return new Camel.CamelBuilder();
    }

    /**
     * The unique ID of the camel. The IDs -1 and -2 are reserved for the two camels running backwards. All other camels start at 0 and are numbered consecutively.
     * (Required)
     */
    public Integer getId() {
        return id;
    }

    /**
     * The color of the camel in the form of a hexadecimal color code
     * (Required)
     */
    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Camel.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null) ? "<null>" : this.id));
        sb.append(',');
        sb.append("color");
        sb.append('=');
        sb.append(((this.color == null) ? "<null>" : this.color));
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
        result = ((result * 31) + ((this.color == null) ? 0 : this.color.hashCode()));
        result = ((result * 31) + ((this.id == null) ? 0 : this.id.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Camel) == false) {
            return false;
        }
        Camel rhs = ((Camel) other);
        return (((this.color == rhs.color) || ((this.color != null) && this.color.equals(rhs.color))) && ((this.id == rhs.id) || ((this.id != null) && this.id.equals(rhs.id))));
    }

    public static class CamelBuilder
        extends Camel.CamelBuilderBase<Camel> {


        public CamelBuilder() {
            super();
        }

    }

    public static abstract class CamelBuilderBase<T extends Camel> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public CamelBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(Camel.CamelBuilder.class)) {
                this.instance = ((T) new Camel());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public Camel.CamelBuilderBase withId(Integer id) {
            ((Camel) this.instance).id = id;
            return this;
        }

        public Camel.CamelBuilderBase withColor(String color) {
            ((Camel) this.instance).color = color;
            return this;
        }

    }

}
