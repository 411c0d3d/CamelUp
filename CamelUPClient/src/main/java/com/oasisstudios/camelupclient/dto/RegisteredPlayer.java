package com.oasisstudios.camelupclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class RegisteredPlayer {
    @JsonProperty("name")
    String name;
    @JsonProperty("id")
    int id;
}
