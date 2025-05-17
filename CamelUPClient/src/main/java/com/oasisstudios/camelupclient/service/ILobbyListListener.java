package com.oasisstudios.camelupclient.service;

import com.oasisstudios.camelupclient.dto.LobbyList;

public interface ILobbyListListener {
    void onLobbyListReceived(LobbyList LobbyListDTO);

    /**
     * On connect.
     */
    void onConnect();
}
