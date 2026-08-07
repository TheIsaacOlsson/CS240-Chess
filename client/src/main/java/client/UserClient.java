package client;

import serverFacade.ChessData.AuthData;
import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.Scanner;

public class UserClient implements Client {
    private final ServerFacade server;
    private final Scanner scanner;
    private final AuthData userAuth;

    public UserClient(ServerFacade server, Scanner scanner, AuthData auth) {
        this.server = server;
        this.scanner = scanner;
        this.userAuth = auth;
    }

    public void run() {
        printToUser("Welcome, " + userAuth.username());
        printToUser(help());

        EvalResult result = new EvalResult("", null, null);
        while (!result.message().equals("logout")) {
            // print ">>> "
            String line = scanner.nextLine();

            try {
                // evaluate result

                if (result.message().equals("joinGame")) {
                    // create new inner repl and pass it the ServerFacade and the scanner
                }
            } catch (Throwable e) {
                // Show the error in the logs, but don't print
            }
        }
        System.out.println();
    }

    @Override
    public EvalResult eval(String input) {
        return new EvalResult("", null, null);
    }

    @Override
    public String help() {
        return "A string that shows what the possible commands are (with displayed input format)";
    }
}