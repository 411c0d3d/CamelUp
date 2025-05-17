package com.oasisstudios.camelupserver.cli;

import com.google.gson.Gson;
import com.oasisstudios.camelupserver.dataaccess.dto.CreateLobby;
import com.oasisstudios.camelupserver.dataaccess.dto.Lobby;
import com.oasisstudios.camelupserver.dataaccess.jsonstorage.LobbyService;
import com.oasisstudios.camelupserver.server.AdminHandler;

import com.oasisstudios.camelupserver.server.TcpSocketServer;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Callable;

import static java.lang.Integer.parseInt;

@Component
@CommandLine.Command(
        name = "admin-cli",
        mixinStandardHelpOptions = true,
        description = "CLI for interacting with AdminHandler on the server"
)
public class AdminCLI implements Callable<Integer> {
    private  AdminHandler adminHandler;
    private TcpSocketServer server = TcpSocketServer.getInstance();
    private Gson gson;


    // CLI interaction method
        private void startCLI() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Admin CLI is running. Type 'exit' to quit.");
    
            while (true) {
                System.out.print("\nEnter command: ");
                String input = scanner.nextLine().trim();
    
                if ("exit".equalsIgnoreCase(input)) {
                    System.out.println("Exiting CLI...");
                    break;
                }
    
                // Handle other commands, such as createLobby
                if ("createLobby".equalsIgnoreCase(input)) {
                    createLobby(scanner);
                } else if (("startGame").equalsIgnoreCase(input)) {
                    startGame(scanner);
                } else if (("joinLobby").equalsIgnoreCase(input)) {
                    joinLobby(scanner);
                } else {
                    System.out.println("Unknown command. Use 'help' for options.");
                }
            }
        }
    
        @CommandLine.Command(name = "createLobby", description = "Create a new lobby using console input")
        public void createLobby(Scanner scanner) {
            adminHandler = server.adminHandler;
            // Ask for lobby name
            System.out.print("Enter Lobby Name: ");
            String lobbyName = scanner.nextLine();
    
            // Ask for lobby JSON (file path)
            System.out.print("Enter config file path: ");
            String lobbyJson = scanner.nextLine();
    
            // Print out the collected inputs
            System.out.println("\nYou entered the following details:");
            System.out.println("Lobby Name: " + lobbyName);
            System.out.println("Lobby JSON: " + lobbyJson);
    
            // Confirm and create lobby
            System.out.print("\nDo you want to create this lobby? (yes/no): ");
            String confirmation = scanner.nextLine();
    
            if ("yes".equalsIgnoreCase(confirmation)) {
                try {
                    adminHandler.createLobbyFromConfig(lobbyJson, lobbyName);
                    System.out.println("\nLobby created successfully!");
                } catch (Exception e) {
                    System.err.println("\nFailed to create lobby: " + e.getMessage());
                }
            } else {
                System.out.println("\nLobby creation cancelled.");
            }
        }
        @CommandLine.Command(name = "joinLobby", description = "Add a player using console input")
        public void joinLobby(Scanner scanner) {
            try{
            adminHandler = server.adminHandler;
            adminHandler.clientHandlersRepository.initialize();
            // Ask for lobby name
            System.out.print("Enter Lobby Id: ");
            String lobbyIdString = scanner.nextLine();
            int lobbyId = Integer.parseInt(lobbyIdString);
            System.out.print("Enter Player Id: ");
            String playerIdString = scanner.nextLine();
            int playerId = Integer.parseInt(playerIdString);
            // Confirm and create lobby
            if (adminHandler.pendingPlayers.stream().noneMatch(p-> Objects.equals(p.getPlayerId(), playerId))) {
                adminHandler.getClientHandlersRepository().addLobbyParticipant(lobbyId, adminHandler.getClientHandlersRepository().getParticipantClientHandlers().stream().filter(p-> p.getClientAck().getClientId() == playerId).findFirst().orElse(null));
            }
            System.out.println("\nPlayer "+ playerId+" joined lobby "+ lobbyId+" successfully!");
            } catch (Exception e) {
                    System.err.println("\nFailed to join lobby: " + e.getMessage());
                }
            }
    
        @Override
        public Integer call() {
            // Start the CLI interaction
        server = TcpSocketServer.getInstance();
        startCLI();
        return 0;
    }

    @CommandLine.Command(name = "startGame", description = "Start the Game after creating the Lobby by using console input")
    public void startGame(Scanner scanner){
        adminHandler = server.adminHandler;


        //Lobby ID fragen
        System.out.print("Enter LobbyId: ");
        String lobbyId = scanner.nextLine();



        System.out.println("\nYou entered the following details:");
        System.out.println("LobbyId: " + lobbyId);

        // Confirm and create lobby
        System.out.print("\nDo you want to start this Game? (yes/no): ");
        String confirmation = scanner.nextLine();
        if ("yes".equalsIgnoreCase(confirmation)) {
        try {
            Lobby startingLobby = LobbyService.readLobbyByFileName(parseInt(lobbyId));
            if (startingLobby == null) {
                System.out.println("\n Lobby nicht vorhanden");
            } else {
                adminHandler.startGame(Integer.valueOf(lobbyId));
                System.out.println("\nGame started successfully!");
            }

        } catch (Exception e) {
            System.err.println("\nFailed to start Game: " + e.getMessage());
        }
        } else {
            System.out.println("\nGame start cancelled.");
        }



    }


}