package com.oasisstudios.camelupengine.engineteilnehmer;

import java.util.function.Consumer;

import com.oasisstudios.camelupengine.dto.GameState;
import com.oasisstudios.camelupengine.dto.Player;

public class GameStateConsumer implements Consumer<GameState> {
    EngineParticipant engine;

    public GameStateConsumer(EngineParticipant engine) {
        this.engine = engine;
    }

    @Override
    public synchronized void accept(GameState gameState) {
        switch (gameState.getGamePhase()) {
            case PLAYING:

                if (gameState.getPlayers().getFirst().getPlayerId() == engine.engineId
                        && gameState.getPlayers().getFirst().getState() == Player.State.PLAYING) {
                    try {
                        wait(gameState.getGameConfig().getThinkingTime()/10);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    engine.engineTurnDecision(gameState);
                    try {
                        wait(200);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
                break;
            case VISUALIZING:
                try {
                    wait(gameState.getGameConfig().getVisualizationTime()-300);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                engine.api.moveVisualized();
                try {
                    wait(200);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                break;
            default:
                break;
        }
    }

}
