package client;

import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.Scanner;

public class UserClient {
    private final ServerFacade server;
    private final Scanner scanner;

    public UserClient(ServerFacade server, Scanner scanner) {
        this.server = server;
        this.scanner = scanner;
    }

    public void run() {
        // Personalized welcome by username
        // help info for registered users

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("logout")) {
            // print ">>> "
            String line = scanner.nextLine();

            try {
                // evaluate result

                if (result.equals("joinGame")) {
                    // create new inner repl and pass it the ServerFacade and the scanner
                }
            } catch (Throwable e) {
                // Show the error in the logs, but don't print
            }
        }
        System.out.println();
    }
}