package com.oasisstudios.camelupadmin.service;

import com.oasisstudios.camelupadmin.dto.Packet;
import org.springframework.stereotype.Service;

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
