package client;

import static ui.EscapeSequences.*;

public interface Client {
    void run();

    String eval(String input);

    String help();

    default void notify(String notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification);
        printPrompt();
        resetOutputStyle();
    }

    default void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_GREEN + ">>> " + SET_TEXT_COLOR_BLACK);
        resetOutputStyle();
    }

    default void resetOutputStyle() {
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_GREEN);
    }
}
