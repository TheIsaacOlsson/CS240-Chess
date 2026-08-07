package client;

import serverFacade.ChessData.AuthData;
import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.Scanner;

public class ChessGameClient implements Client {
    private final ServerFacade server;
    private final Scanner scanner;

    public ServerFacade getServer() { return server; }
    public String startupMessage() { return "Ready to play?"; }
    public String exitCondition() { return "leave"; }
    public boolean hasChildREPL() { return false; }
    public String moveToChildCondition() { return null; }
    public void runChildREPL(ServerFacade server, Scanner scanner, AuthData userAuth) {}

    public ChessGameClient(ServerFacade server, Scanner scanner, AuthData auth) {
        this.server = server;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        printToUser("Game Joined Successfully");
        // For this phase, just print the gameboard from each side.
    }

    @Override
    public String help() {
        return "";
    }

    @Override
    public EvalResult eval(String input) {
        return new EvalResult("", null, null);
    }
}
