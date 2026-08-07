package client;

import static ui.EscapeSequences.*;

public interface Client {
    void run();

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
