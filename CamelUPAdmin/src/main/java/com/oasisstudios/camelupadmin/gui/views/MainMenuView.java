package com.oasisstudios.camelupadmin.gui.views;

import com.oasisstudios.camelupadmin.GUIApplication;
import com.oasisstudios.camelupadmin.gui.ColorConverter;
import com.oasisstudios.camelupadmin.gui.components.CircularIconWidget;
import com.oasisstudios.camelupadmin.gui.components.Clickable;
import com.oasisstudios.camelupadmin.service.NavigationManager;
import com.oasisstudios.camelupadmin.api.Api;



import com.oasisstudios.camelupadmin.service.ContextProvider;
import com.oasisstudios.camelupadmin.service.CurrentUserInfoService;
import com.oasisstudios.camelupadmin.service.UserType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.util.Objects;

import javax.inject.Inject;




/**
 * The Main menu View.
 */
@Component
public class MainMenuView extends AbstractSceneBasedView {

    /**
     * The Current user info service.
     */
    CurrentUserInfoService currentUserInfoService;
    NavigationManager navigationManager;
    
    @Inject
    Api api;
    /**
     * The Gui application.
     */
    GUIApplication guiApplication;

    // Hauptlayout für die Szene
    private final BorderPane mainLayout = new BorderPane();

    private final Image camelUpLogoImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/static/images/logos/CamelUP_Logo.png")));
    private final Image oasisLogoImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/static/images/logos/OasisStudios_Logo.png")));
    private final Image adminImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/static/images/icons/admin_icon.png")));
    private final Image playImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/static/images/icons/player_icon.png")));
    private final Image spectateImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/static/images/icons/beobachter_icon.png")));
    private final ImageView oasisLogoView = new ImageView(oasisLogoImage);
    private final ImageView camelUpLogoView = new ImageView(camelUpLogoImage);

    private final HBox roleSelectorContainer = new HBox();
    private final VBox nameInputContainer = new VBox();

    private final VBox adminBox = new VBox();
    private final VBox spectatorBox = new VBox();
    private final VBox playerBox = new VBox();
    private final VBox topContainer = new VBox();

    private final Label adminLabel = new Label("Admin");
    private final Label playerLabel = new Label("Player");
    private final Label spectatorLabel = new Label("Spectator");

    private final Label playerNameLabel = new Label("ENTER YOUR NAME:");
    private final TextField userNameField = new TextField();
    private final Label playerNameFeedback = new Label();

    private final Label spectator = new Label("Spectator");
    private final Label player = new Label("Player");
    private final Label admin = new Label("Admin");

    private final StackPane headerContainer = new StackPane();
    private final StackPane footerContainer = new StackPane();

    private final HBox specatetBoxLogo = new HBox();
    private final HBox playerBoxLogo = new HBox();
    private final HBox adminBoxLogo = new HBox();
    private final HBox nameBox = new HBox();


    /**
     * Instantiates a new Main menu.
     *
     * @param stage the stage
     */
    public MainMenuView(Stage stage) {
        this.setStage(stage);
        mainLayout.setStyle("-fx-background-color: #FEFEFF");
        currentUserInfoService = ContextProvider.getContext().getBean(CurrentUserInfoService.class);
        navigationManager = ContextProvider.getContext().getBean(NavigationManager.class);
        guiApplication = ContextProvider.getContext().getBean(GUIApplication.class);
        api = ContextProvider.getContext().getBean(Api.class);
        styleComponents();
        configureLayouts();

    }

    private void styleComponents() {
        // Styling der Labels
        styleLabel(adminLabel, 15, "#593B3B");
        styleLabel(playerLabel, 15, "#593B3B");
        styleLabel(spectatorLabel, 15, "#593B3B");
        styleLabel(playerNameLabel, 15, "#A69292");
        styleLabel(playerNameFeedback, 15, ColorConverter.ColorToHex(Color.RED));

        userNameField.setPromptText("z.B. Luke");
        playerNameLabel.setFont(Font.loadFont(getClass().getResourceAsStream("/static/images/Fonts/LilitaOne-Regular.ttf"), 15));
        userNameField.setStyle("-fx-background-color: transparent; -fx-text-fill: #F2A649;");
        userNameField.textProperty().addListener(observable -> onTextHasChanged());
        userNameField.setMaxSize(200,30);
        userNameField.setPrefSize(200,30);
        userNameField.setFont(Font.loadFont(getClass().getResourceAsStream("/static/images/Fonts/LilitaOne-Regular.ttf"), 25));


        // Text Styling
        styleLabel(spectator, 10, "#FFFFFF");
        styleLabel(player, 10, "#FFFFFF");
        styleLabel(admin, 10, "#FFFFFF");

        // Logos anpassen
        camelUpLogoView.setFitWidth(245);
        camelUpLogoView.setFitHeight(215);
        oasisLogoView.setFitWidth(75);
        oasisLogoView.setFitHeight(38);

        this.stageTitle = "Main Menu";
        this.setLayout(mainLayout);
    }

    private void styleLabel(Label label, int fontSize, String color) {
        label.setFont(Font.font("Lilita One", FontWeight.BOLD, fontSize));
        label.setStyle("-fx-text-fill: " + color + ";");
    }

    private void configureLayouts() {
        headerContainer.getChildren().addAll(camelUpLogoView);
        headerContainer.setAlignment(Pos.CENTER);

        footerContainer.setAlignment(Pos.CENTER);
        footerContainer.getChildren().add(oasisLogoView);

        // Rollenbereiche konfigurieren
        var adminRole = new Clickable(new CircularIconWidget(adminLabel, adminImage, 40, "#593B3B"));
        adminRole.setOnClick(e -> onClicked(UserType.ADMIN));

        // Boxen für Rollen erstellen
        configureRoleBox(adminBox, adminRole, adminLabel);

        // Container für Rollenwahl konfigurieren
        roleSelectorContainer.getChildren().addAll(spectatorBox, playerBox, adminBox);
        roleSelectorContainer.setAlignment(Pos.CENTER);
        roleSelectorContainer.setStyle("-fx-background-color: #F5EFE5; -fx-background-radius: 16;");
        roleSelectorContainer.setMaxSize(300, 150);

        // Namenseingabe konfigurieren
        nameInputContainer.getChildren().addAll(playerNameLabel, userNameField);
        nameInputContainer.setAlignment(Pos.CENTER_LEFT);
        nameInputContainer.setPadding(new Insets(10,10,10,10));
        nameInputContainer.setMaxSize(250, 85);
        nameInputContainer.setPrefSize(250, 85);
        nameInputContainer.setStyle("-fx-background-color: #593B3B;-fx-background-radius: 16");


        // Oberer Bereich der Szene
        topContainer.getChildren().addAll(
                headerContainer,
                new Separator() {{
                    setVisible(false);
                    setPrefHeight(150);
                }},
                nameInputContainer,
                playerNameFeedback);
        topContainer.setAlignment(Pos.CENTER);

        // Layout Konfiguration
        mainLayout.setTop(topContainer);
        mainLayout.setBottom(footerContainer);
        mainLayout.setCenter(roleSelectorContainer);
        mainLayout.setPadding(new Insets(20, 20, 20, 20));
    }

    private void configureRoleBox(VBox roleBox, StackPane rolePane, Label roleLabel) {
        roleBox.getChildren().addAll(rolePane, roleLabel);
        roleBox.setAlignment(Pos.CENTER);
    }

    /**
     * On clicked.
     *
     * @param userType the user type
     */
    void onClicked(UserType userType) {
        var userTypeName = userType.name().toLowerCase();
        var userName = userNameField.getText();
        userTypeName = userTypeName.substring(0, 1).toUpperCase() + userTypeName.substring(1).toLowerCase();

        if (userName == null || userName.length() < 4) {
            playerNameFeedback.setText(userTypeName+" name required! ( At least 4 characters long.)");
            return;
        }

        currentUserInfoService.updateUserInfo(currentUserInfoService.getUserId(), userNameField.getText(), userType);
        AbstractSceneBasedView view;
        switch (userType) {
            case ADMIN:
                view = new GameConfigView(this.getPrimaryStage());
                navigationManager.switchScene(view);
                break;
        }
    }

    /**
     * On text has changed.
     */
    void onTextHasChanged(){
        playerNameFeedback.setText("");
    }
}
