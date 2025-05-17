package com.oasisstudios.camelupadmin.gui.components;

import com.oasisstudios.camelupadmin.api.Api;
import com.oasisstudios.camelupadmin.dto.*;
import com.oasisstudios.camelupadmin.service.ContextProvider;

import com.oasisstudios.camelupadmin.service.IMessageListener;
import javafx.application.Platform;
import javafx.geometry.Pos;

import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import lombok.Getter;

import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;

@Component
public class ConnectionStatusWidget implements IMessageListener {

    @Inject
    private Api api;
//    private ClientAck connectionClient;
    private Label statusLabel;
    private Circle statusLight;
    private Color statusColor = Color.RED;
    @Getter
    public VBox widget;

    public ConnectionStatusWidget() {
        api = ContextProvider.getContext().getBean(Api.class);
        api.setMessageListener(this);
        statusLight = new Circle(10);
        setStatusLightStyle(statusColor);
        this.statusLabel = new Label("Offline");
        widget = new VBox(statusLight, statusLabel);
        widget.setAlignment(Pos.TOP_CENTER);
        connectToServer("localhost", 62263);
    }

    private void setStatusLightStyle(Color color) {
        Color darkColor = color == Color.GREEN ? Color.DARKGREEN : Color.DARKRED;
        Color lightColor = color == Color.GREEN ? Color.LIGHTGREEN : Color.RED;
        statusLight.setFill(new RadialGradient(
                0, 0, 0.5, 0.5, 1, true,
                javafx.scene.paint.CycleMethod.NO_CYCLE,
                new Stop(0.0, lightColor),
                new Stop(0.8, color),
                new Stop(1.0, darkColor)
        ));

        statusLight.setStroke(Color.BLACK);
        statusLight.setStrokeWidth(2);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.SILVER);
        shadow.setRadius(20);
        shadow.setOffsetX(15);
        shadow.setOffsetY(10);
        shadow.setBlurType(BlurType.GAUSSIAN);
        statusLight.setEffect(shadow);
    }

    @Override
    public void onMessageReceived(String response) {
        this.onConnect();
        Platform.runLater(() -> {
            System.out.println("Message\t" + response);
        });
    }

    @Override
    public void onPacketReceived(Packet packet) {
        this.onConnect();
    }

    @Override
    public void onConnect() {
        Platform.runLater(() -> {
            setStatusLightStyle(Color.GREEN);
            statusLabel.setText("Online");
        });
    }

    @Override
    public void onDisconnect() {
        Platform.runLater(() -> {
            setStatusLightStyle(Color.RED);
            statusLabel.setText("Offline");
        });
    }

    public void connectToServer(String host, int port) {
        try {
            this.api.connect(host, port)
                    .thenAccept(adminChannel -> {
                        System.out.println("Connected successfully: " + adminChannel);
                        this.onConnect();
                    })
                    .exceptionally(ex -> {
                        this.onDisconnect();
                        System.err.println("Connection failed: " + ex.getMessage());
                        return null;
                    });
        } catch (IOException e) {
            System.err.println("Connection setup failed: " + e.getMessage());
        }
    }
}