package com.oasisstudios.camelupadmin.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PlayerFinalBet {

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
