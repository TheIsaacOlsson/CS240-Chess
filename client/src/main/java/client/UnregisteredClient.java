package client;

import serverFacade.ChessData.AuthData;
import serverFacade.ChessData.UserData;
import serverFacade.RequestResponse.LoginRequest;
import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class UnregisteredClient implements Client {
    private final ServerFacade server;
    private Map<String, Object> clientData = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    public Scanner getScanner() { return scanner; }
    public String startupMessage() { return "Welcome to the chess server! Log in to start."; }
    public String exitCondition() { return "quit"; }
    public boolean hasChildREPL() { return true; }
    public String moveToChildCondition() { return "authorized"; }
    public void runChildREPL() {
        new UserClient(server, scanner, clientData).run();

        printToUser(help());
    }

    public UnregisteredClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
    }

    @Override
    public EvalResult eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> signIn(params);
                case "quit" -> new EvalResult("quit", null);
                default -> new EvalResult(help(), null);
            };
        } catch (ResponseException ex) {
            return new EvalResult("Error", ex);
        }
    }

    @Override
    public String help() {
        return String.format(
                """
                Type one of these commands to navigate:
                - Register as a new user >>> %s register <username> <password> <email> %s
                - Sign in to your account >>> %s login <username> <password> %s
                - See this message again >>> %s help %s
                - Exit the program >>> %s quit %s
                """,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE
                );
    }

    public EvalResult register(String[] params) throws ResponseException {
        // register <username> <password> <email>
        if (params.length >= 3) {
            var response = server.register(new UserData(params[0], params[1], params[2]));
            clientData.put("username", response.username());
            clientData.put("authToken", response.authToken());
            return new EvalResult("authorized", null);
        } else {
            throw new ResponseException(400, "Expected: register <username> <password> <email>");
        }
    }

    public EvalResult signIn(String[] params) {
        // signin <username> <password>
        if (params.length >= 2) {
            var response = server.login(new LoginRequest(params[0], params[1]));
            clientData.put("username", response.username());
            clientData.put("authToken", response.authToken());
            return new EvalResult("authorized", null);
        } else {
            throw new ResponseException(400, "Expected: login <username> <password>");
        }
    }
}
