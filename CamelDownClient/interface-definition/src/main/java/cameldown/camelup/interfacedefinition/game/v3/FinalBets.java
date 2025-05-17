package cameldown.camelup.interfacedefinition.game.v3;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;


/**
 * The two stacks for the final bets.
 */
@Generated("jsonschema2pojo")
public class FinalBets {

    /**
     * The IDs of the players who bet on the first camel. The first element in the list is the first bet placed, and the last is the last bet placed.
     */
    @SerializedName("firstCamel")
    @Expose
    @Valid
    private List<PlayerFinalBet> firstCamel = new ArrayList<PlayerFinalBet>();
    /**
     * The IDs of the players who bet on the last camel. The first element in the list is the first bet placed, and the last is the last bet placed.
     */
    @SerializedName("lastCamel")
    @Expose
    @Valid
    private List<PlayerFinalBet> lastCamel = new ArrayList<PlayerFinalBet>();

    public static FinalBets.FinalBetsBuilderBase builder() {
        return new FinalBets.FinalBetsBuilder();
    }

    /**
     * The IDs of the players who bet on the first camel. The first element in the list is the first bet placed, and the last is the last bet placed.
     */
    public List<PlayerFinalBet> getFirstCamel() {
        return firstCamel;
    }

    /**
     * The IDs of the players who bet on the last camel. The first element in the list is the first bet placed, and the last is the last bet placed.
     */
    public List<PlayerFinalBet> getLastCamel() {
        return lastCamel;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FinalBets.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("firstCamel");
        sb.append('=');
        sb.append(((this.firstCamel == null) ? "<null>" : this.firstCamel));
        sb.append(',');
        sb.append("lastCamel");
        sb.append('=');
        sb.append(((this.lastCamel == null) ? "<null>" : this.lastCamel));
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
        result = ((result * 31) + ((this.firstCamel == null) ? 0 : this.firstCamel.hashCode()));
        result = ((result * 31) + ((this.lastCamel == null) ? 0 : this.lastCamel.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FinalBets) == false) {
            return false;
        }
        FinalBets rhs = ((FinalBets) other);
        return (((this.firstCamel == rhs.firstCamel) || ((this.firstCamel != null) && this.firstCamel.equals(rhs.firstCamel))) && ((this.lastCamel == rhs.lastCamel) || ((this.lastCamel != null) && this.lastCamel.equals(rhs.lastCamel))));
    }

    public static class FinalBetsBuilder
        extends FinalBets.FinalBetsBuilderBase<FinalBets> {


        public FinalBetsBuilder() {
            super();
        }

    }

    public static abstract class FinalBetsBuilderBase<T extends FinalBets> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public FinalBetsBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(FinalBets.FinalBetsBuilder.class)) {
                this.instance = ((T) new FinalBets());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public FinalBets.FinalBetsBuilderBase withFirstCamel(List<PlayerFinalBet> firstCamel) {
            ((FinalBets) this.instance).firstCamel = firstCamel;
            return this;
        }

        public FinalBets.FinalBetsBuilderBase withLastCamel(List<PlayerFinalBet> lastCamel) {
            ((FinalBets) this.instance).lastCamel = lastCamel;
            return this;
        }

    }

}
