package client;

import serverFacade.ChessData.AuthData;
import serverFacade.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public interface Client {
    ServerFacade getServer();
    String startupMessage();
    String exitCondition();
    boolean hasChildREPL();
    String moveToChildCondition();
    void runChildREPL(ServerFacade server, Scanner scanner, AuthData userAuth);

    default void run() {
        printToUser(startupMessage());
        printToUser(help());

        Scanner scanner = new Scanner(System.in);
        EvalResult result = new EvalResult("", null, null);
        while (!result.message().equals(exitCondition())) {
            printPrompt();
            String line = scanner.nextLine();

            result = eval(line);
            // if error, sanitize and return relevant information

            printToUser(result.message());

            if (hasChildREPL() && result.message().equals(moveToChildCondition())) {
                runChildREPL(getServer(), scanner, result.auth());
            }
        }
        printToUser("");
    }

    EvalResult eval(String input);

    String help();

    default void notify(String notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification);
        printPrompt();
        resetOutputStyle();
    }

    default void printToUser(String output) {
        resetOutputStyle();
        System.out.println(output);
    }

    default void printPrompt() {
        System.out.print("\n" + RESET_BG_COLOR + ">>> ");
    }

    default void resetOutputStyle() {
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLUE);
    }
}
