package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import serverFacade.ResponseException;
import serverFacade.ServerFacade;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.GameLoad;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class ChessGameClient implements Client, NotificationHandler {
    private final WebSocketFacade websocket;
    private final Scanner scanner;
    private Map<String, Object> clientData;
    private final boolean orientation;
    private final boolean spectating;

    public Scanner getScanner() { return scanner; }
    public String startupMessage() { if (spectating) {return "Now spectating";} else {return "Get ready to play";} }
    public String exitCondition() { return "leave"; }
    public boolean hasChildREPL() { return false; }
    public String moveToChildCondition() { return null; }
    public void runChildREPL() {}

    public ChessGameClient(Scanner scanner, Map<String, Object> clientData) {
        this.websocket = new WebSocketFacade((String) (clientData.get("serverUrl")), this);
        this.scanner = scanner;
        this.clientData = clientData;

        assert clientData.containsKey("playerColor");
        ChessGame.TeamColor team = (ChessGame.TeamColor) clientData.get("playerColor");
        spectating = team == null;
        orientation = team != null && team.equals(BLACK);
    }

    @Override
    public String help() {
        return String.format(
                """
                Type one of these commands to navigate:
                ...
                """
                // SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE
        );
    }

    @Override
    public EvalResult eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                // Add methods
                default -> new EvalResult(help(), null);
            };
        } catch (ResponseException ex) {
            return new EvalResult("Error", ex);
        }
    }

    @Override
    public void notify(ServerMessage notification) {
        switch (notification.getServerMessageType()) {
            case NOTIFICATION -> printToUser(((Notification) notification).message);
            case ERROR -> printToUser(((ErrorMessage) notification).message);
            case LOAD_GAME -> BoardPrinter.printBoard(((GameLoad) notification).gameState, orientation);
        }
    }
}
