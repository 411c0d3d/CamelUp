package com.oasisstudios.camelupengine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Data
public class BoardSpace {

    @JsonProperty("spaceId")
    private int spaceId;    // unique identifier of tile

    @JsonProperty("camelIds")
    private List<Integer> camelIds;    // IDs of camels on tile

    @JsonProperty("playerCard")
    private PlayerCard playerCard;
}