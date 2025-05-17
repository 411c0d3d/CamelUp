package com.oasisstudios.camelupadmin.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.oasisstudios.camelupadmin.dto.*;
import com.oasisstudios.camelupadmin.service.IMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.lang.System.out;

/**
 * Client handling connections to the server and parsing of json
 */
@RequiredArgsConstructor
public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    @Setter
    private IMessageListener messageListener;
    private final Consumer<Packet> packetEventHandler;
    private final Gson gson;
    private Socket clientSocket;
    private JsonReader in;
    private JsonWriter out;

    /**
     * Connects to the server
     *
     * @param host the domain name or the ip address of the host to connect to, null for the loopback
     * @param port port
     */
    public synchronized void connect(final String host, final int port) throws IOException {
        if (clientSocket != null && clientSocket.isConnected()) {
            messageListener.onConnect();
            logger.error("client is already connected");
            return;
        }
        try {
            clientSocket = new Socket(host, port);
            logger.info("local TCP port: {}", clientSocket.getLocalPort());
            out = new JsonWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            in = new JsonReader(new InputStreamReader(clientSocket.getInputStream()));
            this.connectAdmin();
            new Thread(() -> {
                Thread.currentThread().setName("ClientConnectionThread");
                while (true) {
                    try {
                       
                        messageListener.onConnect();
                        logger.trace("Waiting for server packet...");
                        JsonObject jsonObject = gson.fromJson(in, JsonObject.class);

                        logger.trace("Received server object: {}", jsonObject);

                        final Packet packet = gson.fromJson(jsonObject, Packet.class);
                        logger.trace("Received server packet: {}", packet);

                        packetEventHandler.accept(packet);
                    } catch (final Exception e) {
                        messageListener.onDisconnect();
                        if (e.getCause() instanceof SocketException) {
                            logger.warn("Server disconnected");
                            handleDisconnect();
                            return;
                        } else {
                            logger.error("Error: ", e);
                        }
                    }
                }
            }).start();

        } catch (final IOException e) {
            logger.warn("Error when initializing connection: " + e);
            handleDisconnect();
            logger.error("Connection Failure" + e.getStackTrace().toString());
        }
    }


    public void connectAdmin(){
        AdminChannel AdminChannel = new AdminChannel();
        try {
            Packet packet = Packet.builder().withType(Packet.Type.ADMIN_CHANNEL).withContent(AdminChannel).build();
            gson.toJson(packet, packet.getClass(), out);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Sends a packet to the server
     *
     * @param packet packet to send
     */
    public synchronized void sendMessage(final Packet packet) {
        if (clientSocket != null && !clientSocket.isConnected()) {
            messageListener.onDisconnect();
            logger.error("Connection Failure");
        }

        logger.info("Sending to server {}", packet);
        gson.toJson(packet, packet.getClass(), out);
        try {
            out.flush(); // send
        } catch (final IOException e) {
            handleDisconnect();
        }
    }

    @SneakyThrows
    private synchronized void handleDisconnect() {
        if (clientSocket != null && !clientSocket.isConnected()) {
            messageListener.onDisconnect();
            return;
        }

        tryClosingStreams();
    }

    private synchronized void tryClosingStreams() {
        for (var closable : Stream.of(out, in, clientSocket).toList()) {
            try {
                if (closable != null) {
                    closable.close();
                }
            } catch (final IOException ignored) {
                
            }
        }
        out = null;
        in = null;
        clientSocket = null;
    }

    public synchronized void close() {
        tryClosingStreams();
    }
}
