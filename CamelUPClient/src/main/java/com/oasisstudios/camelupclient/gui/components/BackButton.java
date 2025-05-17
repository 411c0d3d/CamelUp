package com.oasisstudios.camelupclient.gui.components;

import com.oasisstudios.camelupclient.service.ContextProvider;
import com.oasisstudios.camelupclient.service.NavigationManager;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;


import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.kordamp.ikonli.materialdesign.MaterialDesign;
import javax.inject.Inject;

@Component
public class BackButton extends Button {

    @Inject
    private NavigationManager navigationManager;

    @Autowired
    public BackButton() {
        this.navigationManager = ContextProvider.getContext().getBean(NavigationManager.class);
        FontIcon backArrowIcon = new FontIcon(MaterialDesign.MDI_CHEVRON_DOUBLE_LEFT);
        backArrowIcon.setIconSize(30);
        
        this.setGraphic(backArrowIcon);

        // Set additional button styling if needed
        this.getStyleClass().add("back-arrow-button"); // Optional, for styling with CSS

//        // Optional: Set button's behavior (e.g., add event handler for going back)
//        this.setOnAction(event -> {
//            navigationManager.navigateBack();
//            System.out.println("Back button clicked!");
//            // You can replace this with your logic to go back to previous screen or action
//        });

//        this.setStyle("-fx-background-color: transparent; -fx-padding: 10px;");
        this.setOnMousePressed(this::onBackPressed);  // Action when clicked
    }

    private void onBackPressed(MouseEvent event) {
        navigationManager.navigateBack();  // Call the navigateBack method
    }
}
