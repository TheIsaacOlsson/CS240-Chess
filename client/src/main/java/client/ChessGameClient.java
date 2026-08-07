package client;

import serverFacade.ChessData.AuthData;
import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.Map;
import java.util.Scanner;

public class ChessGameClient implements Client {
    private final ServerFacade server;
    private final Scanner scanner;
    private Map<String, Object> clientData;

    public Scanner getScanner() { return scanner; }
    public String startupMessage() { return "Ready to play?"; }
    public String exitCondition() { return "leave"; }
    public boolean hasChildREPL() { return false; }
    public String moveToChildCondition() { return null; }
    public void runChildREPL() {}

    public ChessGameClient(ServerFacade server, Scanner scanner, Map<String, Object> clientData) {
        this.server = server;
        this.scanner = scanner;
        this.clientData = clientData;
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
        return new EvalResult("", null);
    }
}
