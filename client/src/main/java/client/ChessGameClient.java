package client;

import chess.ChessGame;
import chess.ChessPosition;
import serverFacade.ResponseException;
import ui.BoardPrinter;
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
import static ui.EscapeSequences.*;

public class ChessGameClient implements Client, NotificationHandler {
    private final WebSocketFacade websocket;
    private final Scanner scanner;
    private Map<String, Object> clientData;
    private final boolean orientation;
    private final boolean spectating;

    public Scanner getScanner() { return scanner; }
    public String startupMessage() { if (spectating) {return "Now spectating";} else {return "Get ready to play";} }
    public String exitCondition() { return "You have left the game"; }
    public boolean hasChildREPL() { return false; }
    public String moveToChildCondition() { return null; }
    public void runChildREPL() {}

    public ChessGameClient(Scanner scanner, Map<String, Object> clientData) throws ResponseException {
        this.websocket = new WebSocketFacade((String) (clientData.get("serverUrl")), this);
        this.scanner = scanner;
        this.clientData = clientData;

        assert clientData.containsKey("playerColor");
        ChessGame.TeamColor team = (ChessGame.TeamColor) clientData.get("playerColor");
        spectating = team == null;
        orientation = team != null && team.equals(BLACK);

        tryConnect();
    }

    @Override
    public String help() {
        if (spectating) {
            return String.format(
                    """
                    Type one of these commands to navigate:
                    See legal moves for a piece >>> %s legal <x0> %s
                    Leave the game >>> %s leave %s
                    Draw the chess board again >>> %s board %s
                    See this message again >>> %s help %s
                    """,
                    SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE,
                    SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE,
                    SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE,
                    SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE
            );
        }
        return String.format(
                """
                Type one of these commands to navigate:
                Move a piece from square to square >>> %s move <x0> <x0> %s
                See legal moves for a piece >>> %s legal <x0> %s
                Leave the game >>> %s leave %s
                Resign the game >>> %s resign %s
                Draw the chess board again >>> %s board %s
                See this message again >>> %s help %s
                """,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE
        );
    }

    @Override
    public EvalResult eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "move" -> new EvalResult(help(), null); // Only for players
                case "legal" -> seeLegal(params);
                case "leave" -> leaveGame();
                case "resign" -> new EvalResult(help(), null); // Only for players
                case "board" -> printBoard();
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
            case ERROR -> printToUser(((ErrorMessage) notification).errorMessage);
            case LOAD_GAME -> printToUser(BoardPrinter.printBoard(((GameLoad) notification).game, ((GameLoad) notification).highlightSquares, orientation));
        }
    }

    public void tryConnect() throws ResponseException {
        websocket.connect((String) clientData.get("authToken"), (Integer) clientData.get("gameID"));
    }

    public EvalResult leaveGame() throws ResponseException {
        websocket.leave((String) clientData.get("authToken"), (Integer) clientData.get("gameID"));
        return new EvalResult("You have left the game", null);
    }

    public EvalResult printBoard() {
        websocket.refresh((String) clientData.get("authToken"), (Integer) clientData.get("gameID"));
        return new EvalResult("", null);
    }

    public EvalResult seeLegal(String[] params) {
        if (params.length >= 1) {
            String location = params[0];
            ChessPosition position = parseLocation(location);
            websocket.seeMoves((String) clientData.get("authToken"), (Integer) clientData.get("gameID"), position);
        } else {
            throw new ResponseException(400, "Expected: legal <position>");
        }
        return new EvalResult("", null);
    }

    private ChessPosition parseLocation(String location) {
        try {
            char letter = location.charAt(0);
            char number = location.charAt(1);

            int row = number - '0';
            int col = letter - 'a'+1;

            if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                return new ChessPosition(row, col);
            } else {
                throw new ResponseException(400, "Position must be between a1 and h8");
            }
        } catch (Exception e) {
            throw new ResponseException(400, "Invalid position");
        }
    }
}
