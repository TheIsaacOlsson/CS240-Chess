package client;

import serverFacade.ChessData.UserData;
import serverFacade.RequestResponse.LoginRequest;
import serverFacade.RequestResponse.RegisterRequest;
import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class UnregisteredClient implements Client {
    private final ServerFacade server;

    public UnregisteredClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        printToUser(" Welcome to the chess server! Log in to start.");
        printToUser(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            result = eval(line);
            resetOutputStyle();
            printToUser(result);

            if (result.equals("authorized")) {
                new UserClient(server, scanner).run();
            }
        }
        printToUser("");
    }

    @Override
    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> signIn(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage(); // Deal with the error better (sanitize before printing)
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
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_GREEN,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_GREEN,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_GREEN,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_GREEN
                );
    }

    public String register(String[] params) throws ResponseException {
        // register <username> <password> <email>
        if (params.length >= 3) {
            var response = server.register(new UserData(params[0], params[1], params[2]));
            return "authorized"; // Return an object with the output message and other internal information (like errors or authTokens)
        } else {
            throw new ResponseException(400, "Expected: register <username> <password> <email>");
        }
    }

    public String signIn(String[] params) {
        // signin <username> <password>
        if (params.length >= 2) {
            var response = server.login(new LoginRequest(params[0], params[1]));
            return "authorized";
        } else {
            throw new ResponseException(400, "Expected: login <username> <password>");
        }
    }
}
