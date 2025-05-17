package com.oasisstudios.camelupserver.dataaccess.dto;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;


/**
 * camel
 * <p>
 * A specific camel
 */
@Generated("jsonschema2pojo")
@Data
public class Camel {
    /**
     * Instantiates a new Camel.
     *
     * @param id    the id
     * @param color the color
     */
    public Camel(Integer id, String color) {
        this.id = id;
        this.color = color;
    }
    public Camel(){}
    /**
     * The unique ID of the camel. The IDs -1 and -2 are reserved for the two camels running backwards. All other camels start at 0 and are numbered consecutively.
     * (Required)
     */
    @SerializedName("id")
//    @Expose
//    @DecimalMin("-2")
//    @DecimalMax("32767")
//    @NotNull
    private Integer id;
//    /**
//     * The color of the camel in the form of a hexadecimal color code
//     * (Required)
//     */
    @SerializedName("color")
//    @Expose
//    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
//    @NotNull
    private String color;
//
//    /**
//     * Builder camel . camel builder base.
//     *
//     * @return the camel . camel builder base
//     */
//    public static Camel.CamelBuilderBase builder() {
//        return new Camel.CamelBuilder();
//    }

    /**
     * The unique ID of the camel. The IDs -1 and -2 are reserved for the two camels running backwards. All other camels start at 0 and are numbered consecutively.
     * (Required)
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * The color of the camel in the form of a hexadecimal color code
     * (Required)
     *
     * @return the color
     */
    public String getColor() {
        return color;
    }

//    @Override
    public String toStringX() {
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

//    @Override
//    public int hashCode() {
//        int result = 1;
//        result = ((result * 31) + ((this.color == null) ? 0 : this.color.hashCode()));
//        result = ((result * 31) + ((this.id == null) ? 0 : this.id.hashCode()));
//        return result;
//    }

//    @Override
//    public boolean equals(Object other) {
//        if (other == this) {
//            return true;
//        }
//        if ((other instanceof Camel) == false) {
//            return false;
//        }
//        Camel rhs = ((Camel) other);
//        return (((this.color == rhs.color) || ((this.color != null) && this.color.equals(rhs.color))) && ((this.id == rhs.id) || ((this.id != null) && this.id.equals(rhs.id))));
//    }

//    /**
//     * The type Camel builder.
//     */
//    public static class CamelBuilder
//        extends Camel.CamelBuilderBase<Camel> {
//
//
//        /**
//         * Instantiates a new Camel builder.
//         */
//        public CamelBuilder() {
//            super();
//        }
//
//    }
//
//    /**
//     * The type Camel builder base.
//     *
//     * @param <T> the type parameter
//     */
//    public static abstract class CamelBuilderBase<T extends Camel> {
//
//        /**
//         * The Instance.
//         */
//        protected T instance;
//
//        /**
//         * Instantiates a new Camel builder base.
//         */
//        @SuppressWarnings("unchecked")
//        public CamelBuilderBase() {
//            // Skip initialization when called from subclass
//            if (this.getClass().equals(Camel.CamelBuilder.class)) {
//                this.instance = ((T) new Camel());
//            }
//        }
//
//        /**
//         * Build t.
//         *
//         * @return the t
//         */
//        public T build() {
//            T result;
//            result = this.instance;
//            this.instance = null;
//            return result;
//        }
//
//        /**
//         * With id camel . camel builder base.
//         *
//         * @param id the id
//         * @return the camel . camel builder base
//         */
//        public Camel.CamelBuilderBase withId(Integer id) {
//            ((Camel) this.instance).id = id;
//            return this;
//        }
//
//        /**
//         * With color camel . camel builder base.
//         *
//         * @param color the color
//         * @return the camel . camel builder base
//         */
//        public Camel.CamelBuilderBase withColor(String color) {
//            ((Camel) this.instance).color = color;
//            return this;
//        }

//    }

}
