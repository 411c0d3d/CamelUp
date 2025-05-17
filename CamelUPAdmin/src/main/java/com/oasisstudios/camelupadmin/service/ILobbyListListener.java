package com.oasisstudios.camelupadmin.service;

import com.oasisstudios.camelupadmin.dto.LobbyList;

public interface ILobbyListListener {
    void onLobbyListReceived(LobbyList LobbyListDTO);

    /**
     * On connect.
     */
    void onConnect();
}
