package client;

import serverFacade.ResponseException;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public interface Client {
    Scanner getScanner();
    String startupMessage();
    String exitCondition();
    boolean hasChildREPL();
    String moveToChildCondition();
    void runChildREPL();

    default void run() {
        printToUser(startupMessage());
        printToUser(help());

        Scanner scanner = getScanner();
        EvalResult result = new EvalResult("", null);
        while (!result.message().equals(exitCondition())) {
            printPrompt();
            String line = scanner.nextLine();

            result = eval(line);
            assert (! (result.message() == null));
            if (result.message().equals("Error")) {
                notify(sanitizeError(result.error()));
                continue;
            }

            printToUser(result.message());

            if (hasChildREPL() && result.message().equals(moveToChildCondition())) {

                runChildREPL();
            }
        }
        printToUser("");
    }

    EvalResult eval(String input);

    String help();

    default void notify(String notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification);
        resetOutputStyle();
    }

    default String sanitizeError(ResponseException error) {
        int code = error.getStatusCode();
        switch (code) {
            case 500 -> {return "Server failure, please try again later.";}
            case 401 -> {return "Unauthorized";}
            case 400 -> {return "Unknown request. Review the commands and try again. Make sure to include each part in one command.";}
            case 403 -> {return "This is already taken.";}
            default -> {return "Unknown error";}
        }
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
