package client;

import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.Scanner;

public class UnregisteredClient {
    private final ServerFacade server;

    public UnregisteredClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        // System.out.println(LOGO + " Welcome to the pet store. Sign in to start.");
        // System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            // print ">>> "
            String line = scanner.nextLine();

            try {
                // evaluate result

                if (result.equals("authorized")) {
                    // create new inner repl and pass it the ServerFacade and the scanner
                }
            } catch (Throwable e) {
                // Show the error in the logs, but don't print
            }
        }
        System.out.println();
    }
}
