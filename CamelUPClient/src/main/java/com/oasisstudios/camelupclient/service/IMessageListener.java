package com.oasisstudios.camelupclient.service;

import org.springframework.stereotype.Service;
import com.oasisstudios.camelupclient.dto.Packet;

/**
 * The interface Message listener.
 */
@Service
public interface IMessageListener {
    /**
     * On message received.
     *
     * @param message the message
     */
    void onMessageReceived(String message);

    void onPacketReceived(Packet packet);

    /**
     * On connect.
     */
    void onConnect();

    /**
     * On disconnect.
     */
    void onDisconnect();
}
