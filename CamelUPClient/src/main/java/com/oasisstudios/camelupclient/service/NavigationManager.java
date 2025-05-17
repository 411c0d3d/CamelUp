package com.oasisstudios.camelupclient.service;

import com.oasisstudios.camelupclient.GUIApplication;
import com.oasisstudios.camelupclient.api.Api;
import com.oasisstudios.camelupclient.gui.views.AbstractSceneBasedView;

import org.springframework.stereotype.Component;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

@Component
public class NavigationManager {
    private Deque<AbstractSceneBasedView> sceneHistory;
    private GUIApplication guiApplication;
    private Api api;
    public void init(AbstractSceneBasedView view) {
        this.guiApplication = ContextProvider.getContext().getBean(GUIApplication.class);
        this.sceneHistory = new ArrayDeque<>();
        this.api = ContextProvider.getContext().getBean(Api.class);
        sceneHistory.push(view);
    }

    public void switchScene(AbstractSceneBasedView view) {
        AbstractSceneBasedView currentView = sceneHistory.peek();
        view.getPrimaryStage().setMaximized(Objects.requireNonNull(currentView).getPrimaryStage().isMaximized());
        this.sceneHistory.push(view);
        guiApplication.showScene(view);
    }

    public void navigateBack() {
        if (sceneHistory.size() > 1) {
            // Remove the current scene and get the previous one
            AbstractSceneBasedView currentView = sceneHistory.peek();
            sceneHistory.pop();
            AbstractSceneBasedView previousView = sceneHistory.peek();
            if (previousView != null) {
                previousView.getPrimaryStage().setMaximized(currentView.getPrimaryStage().isMaximized());
                guiApplication.showScene(previousView);
            }
            api.leaveLobby();
        }
    }
}