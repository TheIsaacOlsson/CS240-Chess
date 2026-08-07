package client;

import static ui.EscapeSequences.*;

public interface Client {
    public void run();

    public String eval(String input);

    public String help();

    public default void notify(String notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification);
        printPrompt();
    }

    default void printPrompt() {
        resetOutputStyle();
        System.out.print("\n" + SET_TEXT_COLOR_GREEN + ">>> " + SET_TEXT_COLOR_BLACK);
    }

    default void resetOutputStyle() {
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
    }
}
