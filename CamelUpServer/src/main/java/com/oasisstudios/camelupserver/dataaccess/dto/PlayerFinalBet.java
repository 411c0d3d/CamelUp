package com.oasisstudios.camelupserver.dataaccess.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

/**
 * The type Player final bet.
 */
@Builder
@Data
public class PlayerFinalBet {
    
    /**
     * Instantiates a new Player final bet.
     *
     * @param playerId the player id
     * @param camelId  the camel id
     */
    public PlayerFinalBet(int playerId, Integer camelId) {
        this.playerId = playerId;
        this.camelId = camelId;
    }

    /**
     * The ID of the player who placed the bet. Corresponds to the clientId of the player.
     */
    @SerializedName("playerId")
    @Expose
    int playerId;

    /**
     * The id of the camel on which the player has bet. Is only sent if the playerId corresponds to that of the receiving player.
     */
    @SerializedName("camelId")
    @Expose
    Integer camelId;
}
