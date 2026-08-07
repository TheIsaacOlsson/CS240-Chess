package client;

import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.Scanner;

public class ChessGameClient {
    private final ServerFacade server;
    private final Scanner scanner;

    public ChessGameClient(ServerFacade server, Scanner scanner) {
        this.server = server;
        this.scanner = scanner;
    }

    public void run() {
        // For this phase, just print the gameboard from each side.
    }
}
