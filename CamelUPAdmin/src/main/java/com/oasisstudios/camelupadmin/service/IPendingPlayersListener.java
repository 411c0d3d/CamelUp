package com.oasisstudios.camelupadmin.service;

import com.oasisstudios.camelupadmin.dto.PendingPlayerList;
import org.springframework.stereotype.Service;

@Service
public interface IPendingPlayersListener {

    public void onPendingPlayersReceived(PendingPlayerList pendingPlayerList);
}
