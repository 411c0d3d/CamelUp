package com.oasisstudios.camelupclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class CreateLobby {
    @JsonProperty("lobby")
    private Lobby lobby;

    /*/
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PlayerRegistration.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("lobby");
        sb.append('=');
        sb.append(((this.lobby == null) ? "<null>" : this.lobby));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }*/
}
