package client;

import serverFacade.ChessData.AuthData;
import serverFacade.ChessData.UserData;
import serverFacade.RequestResponse.LoginRequest;
import serverFacade.RequestResponse.RegisterRequest;
import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class UnregisteredClient implements Client {
    private final ServerFacade server;

    public UnregisteredClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        printToUser(" Welcome to the chess server! Log in to start.");
        printToUser(help());

        Scanner scanner = new Scanner(System.in);
        EvalResult result = new EvalResult("", null, null);
        while (!result.message().equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            result = eval(line);
            // if error, sanitize and return relevant information

            resetOutputStyle();
            printToUser(result.message());

            if (result.message().equals("authorized")) {
                new UserClient(server, scanner, result.auth()).run();
            }
        }
        printToUser("");
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
                case "quit" -> new EvalResult("quit", null, null);
                default -> new EvalResult(help(), null, null);
            };
        } catch (ResponseException ex) {
            return new EvalResult("Error", null, ex);
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
            return new EvalResult("authorized", new AuthData(response.authToken(), response.username()), null);
        } else {
            throw new ResponseException(400, "Expected: register <username> <password> <email>");
        }
    }

    public EvalResult signIn(String[] params) {
        // signin <username> <password>
        if (params.length >= 2) {
            var response = server.login(new LoginRequest(params[0], params[1]));
            return new EvalResult("authorized", new AuthData(response.authToken(), response.username()), null);
        } else {
            throw new ResponseException(400, "Expected: login <username> <password>");
        }
    }
}
