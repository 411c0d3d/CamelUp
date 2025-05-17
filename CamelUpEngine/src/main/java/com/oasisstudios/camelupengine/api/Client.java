package com.oasisstudios.camelupengine.api;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.oasisstudios.camelupengine.dto.Packet;

import lombok.RequiredArgsConstructor;
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

/**
 * Client handling connections to the server and parsing of json
 */
@RequiredArgsConstructor
public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

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
            throw new IllegalStateException("client is already connected");
        }

        try {
            clientSocket = new Socket(host, port);
            logger.info("local TCP port: {}", clientSocket.getLocalPort());

            out = new JsonWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            in = new JsonReader(new InputStreamReader(clientSocket.getInputStream()));

            new Thread(() -> {
                Thread.currentThread().setName("ClientConnectionThread");

                while (true) {
                    try {
                        logger.trace("Waiting for server packet...");
                        JsonObject jsonObject = gson.fromJson(in, JsonObject.class);

                        logger.trace("Received server object: {}", jsonObject);

                        final Packet packet = gson.fromJson(jsonObject, Packet.class);
                        logger.trace("Received server packet: {}", packet);

                        packetEventHandler.accept(packet);
                    } catch (final Exception e) {
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
            throw e;
        }
    }

    /**
     * Sends a packet to the server
     *
     * @param packet packet to send
     */
    public synchronized void sendMessage(final Packet packet) {
        if (clientSocket != null && !clientSocket.isConnected()) {
            throw new IllegalStateException("client is not connected");
        }

        logger.trace("Sending to server {}", packet);
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
