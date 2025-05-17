package com.oasisstudios.camelupserver.cli;

import com.oasisstudios.camelupserver.server.TcpSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
public class CLIExecutor implements CommandLineRunner, SmartLifecycle {

    private final AdminCLI adminCLI;
    private final TcpSocketServer tcpSocketServer;
    private boolean isRunning = false;

    @Autowired
    public CLIExecutor(TcpSocketServer tcpSocketServer, AdminCLI adminCLI) {
        this.adminCLI = adminCLI;
        this.tcpSocketServer = tcpSocketServer;
    }

    @Override
    public void run(String... args) {
        // This will execute the AdminCLI once TcpSocketServer is running
        if (tcpSocketServer.isRunning()) {
            CommandLine commandLine = new CommandLine(adminCLI);
            int exitCode = commandLine.execute(args);
            if (exitCode != 0) {
                System.err.println("CLI execution failed.");
            }
        } else {
            System.out.println("Server is not running. Cannot start CLI.");
        }
    }

    /**
     * 
     */
    @Override
    public void start() {
        // Wait until TcpSocketServer is running
        while (!tcpSocketServer.isRunning()) {
            try {
                Thread.sleep(1000); // Polling until the server is up
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Ensure CLI starts after TcpSocketServer has fully started
        System.out.println("TCP Socket Server started. CLIExecutor is starting.");
        isRunning = true;

        // Now run the AdminCLI (this starts the prompt)
        CommandLine commandLine = new CommandLine(adminCLI);
        commandLine.execute();  // You can pass arguments here if needed
    }

    /**
     * 
     */
    @Override
    public void stop() {
        System.out.println("Stopping CLIExecutor...");
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE; // Ensure the CLIExecutor starts after the server
    }

    @Override
    public boolean isAutoStartup() {
        return SmartLifecycle.super.isAutoStartup();
    }
}
