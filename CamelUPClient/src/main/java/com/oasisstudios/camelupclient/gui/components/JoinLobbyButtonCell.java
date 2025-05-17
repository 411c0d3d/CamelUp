package com.oasisstudios.camelupclient.gui.components;

import java.util.concurrent.Future;

import javax.inject.Inject;

import com.oasisstudios.camelupclient.GUIApplication;
import com.oasisstudios.camelupclient.api.Api;
import com.oasisstudios.camelupclient.dto.*;
import com.oasisstudios.camelupclient.gui.views.AbstractSceneBasedView;
import com.oasisstudios.camelupclient.gui.views.GamesSelectionView;
import com.oasisstudios.camelupclient.gui.views.SpectatorView;
import com.oasisstudios.camelupclient.service.ContextProvider;
import com.oasisstudios.camelupclient.service.NavigationManager;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class JoinLobbyButtonCell extends TableCell<Lobby, Void> {

    @Inject
    private Api api;
    private final Button spectateButton = new Button("Spectate");
    NavigationManager navigationManager;
    
    public JoinLobbyButtonCell(GUIApplication application, GamesSelectionView parentView) {
        navigationManager = ContextProvider.getContext().getBean(NavigationManager.class);
        api = ContextProvider.getContext().getBean(Api.class);
        spectateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Lobby selectedLobby = getTableRow().getItem();
                if (selectedLobby != null) {
                    joinLobby(selectedLobby.getLobbyId(), application, parentView);
                }
            }
        });
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(spectateButton);
        }
    }

    public void joinLobby(int lobbyId, GUIApplication guiApplication, GamesSelectionView parentView) {
        //To join as Spectator
        Future<SuccessFeedback> future = api.joinLobby(lobbyId, false);
        System.out.println("Joining Lobby with ID: " + lobbyId);
        
        Stage stage = guiApplication.getPrimaryStage();
        AbstractSceneBasedView view = new SpectatorView(stage, new Lobby());
        guiApplication.showScene(view);
        navigationManager.switchScene(view);
        parentView.stop();
    }
}
