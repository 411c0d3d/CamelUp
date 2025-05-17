package com.oasisstudios.camelupserver.domain.repository;

import com.oasisstudios.camelupserver.server.ParticipantHandler;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The type Client handlers repository.
 */
@Setter @Getter
public class ClientHandlersRepository {
    
    private static ClientHandlersRepository instance;
    private CopyOnWriteArraySet<ParticipantHandler> participantClientHandlers;
    private ConcurrentHashMap<Integer, CopyOnWriteArraySet<ParticipantHandler>> lobbyParticipants;
    private ClientHandlersRepository(){};

    /**
     * Get client handlers repository client handlers repository.
     *
     * @return the client handlers repository
     */
    public static synchronized ClientHandlersRepository getClientHandlersRepository(){
        if(instance == null){
            instance = new ClientHandlersRepository();
        }
        return instance;
    }

    /**
     * Initialize.
     */
    public void initialize() {
        participantClientHandlers = new CopyOnWriteArraySet<>();
        lobbyParticipants = new ConcurrentHashMap<>();
    }

    /**
     * Update participant client handler.
     *
     * @param participantClientHandlers the participant client handlers
     */
    public void updateParticipantClientHandler(CopyOnWriteArraySet<ParticipantHandler> participantClientHandlers){
        this.participantClientHandlers = participantClientHandlers;
    }

    /**
     * Add participant client handler.
     *
     * @param participantClientHandler the participant client handler
     */
    public void addParticipantClientHandler(ParticipantHandler participantClientHandler){
        participantClientHandlers.add(participantClientHandler);
    }

    /**
     * Remove participant client handler.
     *
     * @param participantClientHandler the participant client handler
     */
    public void removeParticipantClientHandler(ParticipantHandler participantClientHandler){
        participantClientHandlers.remove(participantClientHandler);
    }

    /**
     * Add lobby participant.
     *
     * @param lobbyId the lobby id 
     * @param participantClientHandler the participant client handler
     */
    public void addLobbyParticipant(int lobbyId, ParticipantHandler participantClientHandler){
        lobbyParticipants.get(lobbyId).add(participantClientHandler);
    }
    
    /**
     * Remove lobby participant.
     *
     * @param lobbyId the lobby id 
     * @param participantClientHandler the participant client handler
     */
    public void removeLobbyParticipant(int lobbyId, ParticipantHandler participantClientHandler){
        lobbyParticipants.get(lobbyId).remove(participantClientHandler);
    }
}
