package com.oasisstudios.camelupserver.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.oasisstudios.camelupserver.dataaccess.dto.Packet;
import com.oasisstudios.camelupserver.domain.repository.ClientHandlersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TcpSocketServer implements SmartLifecycle {
    static TcpSocketServer instance;
    private static final int PORT = 62263;
    private static final Logger logger = LoggerFactory.getLogger(TcpSocketServer.class);
    private final Gson gson = new Gson();
    private final AtomicInteger clientId = new AtomicInteger(1);
    private volatile boolean running = false;
    private Thread serverThread;
    private final ExecutorService clientThreadPool = Executors.newCachedThreadPool();
    private final ClientHandlersRepository clientHandlersRepository = ClientHandlersRepository
            .getClientHandlersRepository();
    public AdminHandler adminHandler = AdminHandler.getInstance(new Socket());
    
    @Override
    public void start() {
        instance = this;
        if (!running) {
            running = true;
            serverThread = new Thread(this::runServer);
            serverThread.start();
            logger.info("Server started on port {}", PORT);
        }
    }

    private void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            clientHandlersRepository.initialize();

            while (running) {
                Socket clientSocket = serverSocket.accept();
                clientThreadPool.submit(() -> handleClient(clientSocket));
                logger.info("New client connected: {}", clientSocket.getRemoteSocketAddress());
            }
        } catch (IOException e) {
            logger.error("Server error: ", e);
        }
    }

    boolean adminWasPicked;

    private void handleClient(Socket clientSocket) {
        try {
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();
            JsonReader reader = new JsonReader(new InputStreamReader(in));
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(out));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Gson gson = new Gson();

            adminWasPicked = false;


            /*ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            @SuppressWarnings({ "rawtypes", "unchecked" })
            final Future handler = executor.submit(new Callable() {

                @Override
                public Object call() throws Exception {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'call'");
                }
            });
            executor.schedule(new Runnable() {
                public void run() {
                    JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
                    Packet packet = gson.fromJson(jsonObject, Packet.class);
                    if (packet != null && packet.getType() == Packet.Type.ADMIN_CHANNEL) {
                        logger.info(packet.toString());
                        try {
                            handleAdmin(clientSocket);
                            logger.info("Admin was made");
                            adminWasPicked = true;
                            handler.cancel(true);
                        } catch (IOException e) {
                            logger.error("Failed admin connection {}", e.getMessage());
                        }
                    }
                }
            }, 1000, TimeUnit.MILLISECONDS);*/
            
            handleParticipant(clientSocket);
            logger.info("Client connection was made");
            
        } catch (IOException e) {
            logger.error("Error handling client: {}", clientSocket.getRemoteSocketAddress(), e);
        }
    }

    private Packet parsePacket(JsonReader reader) {
        try {
            return gson.fromJson(reader, Packet.class);
        } catch (JsonSyntaxException e) {
            logger.error("Failed to parse packet", e);
            return null;
        }
    }

    private void handleAdmin(Socket clientSocket) throws IOException {
        AdminHandler adminHandler = AdminHandler.getInstance(clientSocket);
        if (!adminHandler.isAlive()) {
            adminHandler.start();
            logger.info("Admin handler started for {}", clientSocket.getRemoteSocketAddress());
        } else {
            logger.info("Admin handler already running");
        }
    }

    private void handleParticipant(Socket clientSocket) throws IOException {
        ParticipantHandler participantHandler = new ParticipantHandler(clientSocket, clientId.incrementAndGet());
        clientHandlersRepository.addParticipantClientHandler(participantHandler);
        participantHandler.start();
        logger.info("Participant handler started for {}", clientSocket.getRemoteSocketAddress());
    }
    public static synchronized TcpSocketServer getInstance() {
        return instance;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public void stop() {
        running = false;
        clientThreadPool.shutdown();
        logger.info("Server stopped.");
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public int getPhase() {
        return SmartLifecycle.super.getPhase();
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
