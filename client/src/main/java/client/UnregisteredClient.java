package client;

import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.Scanner;

public class UnregisteredClient implements Client {
    private final ServerFacade server;

    public UnregisteredClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        // System.out.println(LOGO + " Welcome to the chess server. Sign in to start.");
        // System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            // print ">>> "
            String line = scanner.nextLine();

            try {
                // evaluate result

                if (result.equals("authorized")) {
                    new UserClient(server, scanner).run();
                }
            } catch (Throwable e) {
                // Show the error in the logs, but don't print
            }
        }
        System.out.println();
    }

    @Override
    public String eval(String input) {
        return "";
    }

    @Override
    public String help() {
        return "A string that shows what the possible commands are (with displayed input format)";
    }
}
