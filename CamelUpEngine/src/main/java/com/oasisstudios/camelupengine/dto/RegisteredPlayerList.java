package com.oasisstudios.camelupengine.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class RegisteredPlayerList {
    @JsonProperty("players")
    private ArrayList<RegisteredPlayer> players;

}
