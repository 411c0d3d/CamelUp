package com.oasisstudios.camelupserver.server;

import com.oasisstudios.camelupserver.dataaccess.dto.Packet;

public interface IPacketListener {
    void onPacketReceived(Packet packet);
}
