package com.oasisstudios.camelupadmin;

import com.oasisstudios.camelupadmin.api.Api;

import com.oasisstudios.camelupadmin.gui.components.BaseLayout;
import com.oasisstudios.camelupadmin.gui.components.Footer;
import com.oasisstudios.camelupadmin.gui.views.AbstractSceneBasedView;
import com.oasisstudios.camelupadmin.gui.views.MainMenuView;

import com.oasisstudios.camelupadmin.service.ContextProvider;
import com.oasisstudios.camelupadmin.service.CurrentUserInfoService;
import com.oasisstudios.camelupadmin.service.NavigationManager;
import com.oasisstudios.camelupadmin.service.UserType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import lombok.Getter;

import lombok.NonNull;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * The main Gui application.
 */
@Component
public class GUIApplication extends Application {
    @Getter
    private Stage primaryStage;
    private Api api;
    private static CurrentUserInfoService currentUserInfoService;
    private static NavigationManager navigationManager;
    private static AnnotationConfigApplicationContext context;

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
        api = ContextProvider.getContext().getBean(Api.class);
        if (currentUserInfoService == null) {
            currentUserInfoService = context.getBean(CurrentUserInfoService.class);
            currentUserInfoService.initializeUserInfo("0", "Admin", UserType.ADMIN);
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
}
