package com.oasisstudios.camelupclient;

import com.oasisstudios.camelupclient.api.Api;
import com.oasisstudios.camelupclient.dto.ClientAck;
import com.oasisstudios.camelupclient.gui.components.BaseLayout;
import com.oasisstudios.camelupclient.gui.components.Footer;
import com.oasisstudios.camelupclient.gui.views.AbstractSceneBasedView;
import com.oasisstudios.camelupclient.gui.views.MainMenuView;

import com.oasisstudios.camelupclient.service.ContextProvider;
import com.oasisstudios.camelupclient.service.CurrentUserInfoService;
import com.oasisstudios.camelupclient.service.NavigationManager;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import lombok.Getter;

import lombok.NonNull;

import java.util.concurrent.Future;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * The main Gui application.
 */
@Component
public class GUIApplication extends Application {
    @Getter
    private static String userId;
    @Getter
    private Stage primaryStage;

    private static CurrentUserInfoService currentUserInfoService;
    private static NavigationManager navigationManager;
    private static AnnotationConfigApplicationContext context;
    private static Api api;

    /**
     * The entry point of the application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        if (ContextProvider.getContext() == null) {
            context = ContextProvider.initializeContext();
            context.getAutowireCapableBeanFactory().autowireBean(GUIApplication.class);
            context.start();
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("APP START");
        if (api == null) {
            api = context.getBean(Api.class);
        }
        try {
            Future<ClientAck> future = api.connect("localhost", 62263);
            ClientAck clientAck = future.get();
            userId = clientAck.getClientId().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (currentUserInfoService == null) {
            currentUserInfoService = context.getBean(CurrentUserInfoService.class);
            currentUserInfoService.initializeUserInfo(userId, "", null);
        }
       
        this.primaryStage = primaryStage;
        var mainMenuView = new MainMenuView(primaryStage);
        if (navigationManager == null) {
            navigationManager = context.getBean(NavigationManager.class);
            navigationManager.init(mainMenuView);
        }
        showScene(mainMenuView);
    }

    /**
     * Show scene.
     *
     * @param view the view
     */
    public void showScene(@NonNull AbstractSceneBasedView view) {
        var title = view.getStageTitle() + " " + currentUserInfoService.getUserId();
        this.primaryStage = view.getPrimaryStage();
        this.primaryStage.setTitle(title);
        Scene newScene;
        var isViewMaxed = view.getPrimaryStage().isMaximized();
        if (isViewMaxed) {
            var root = new BaseLayout(view.getLayout(), Footer.getInstance());
            view.ConfigureMaxedStage(root);
            newScene  = new Scene(root);
        }else {
            newScene = new Scene(new BaseLayout(view.getLayout(), Footer.getInstance()), 1280, 720);
        }
        this.primaryStage.setScene(newScene);
        this.primaryStage.setMaximized(isViewMaxed);
        this.primaryStage.show();
    }
    @Override
    public void stop() throws Exception {
        System.exit(0);
    }
    
}
