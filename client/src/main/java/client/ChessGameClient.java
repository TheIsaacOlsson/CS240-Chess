package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import serverFacade.ResponseException;
import serverFacade.ServerFacade;
import websocket.NotificationHandler;
import websocket.messages.ErrorMessage;
import websocket.messages.GameLoad;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.util.Map;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class ChessGameClient implements Client, NotificationHandler {
    private final ServerFacade server;
    private final Scanner scanner;
    private Map<String, Object> clientData;
    private final boolean orientation;

    public Scanner getScanner() { return scanner; }
    public String startupMessage() { return "Game ID: " + clientData.get("gameID"); }
    public String exitCondition() { return "leave"; }
    public boolean hasChildREPL() { return false; }
    public String moveToChildCondition() { return null; }
    public void runChildREPL() {}

    public ChessGameClient(ServerFacade server, Scanner scanner, Map<String, Object> clientData) {
        this.server = server;
        this.scanner = scanner;
        this.clientData = clientData;
        ChessGame.TeamColor team = (ChessGame.TeamColor) clientData.get("playerColor");
        orientation = team != null && team.equals(BLACK);
    }

    @Override
    public void run() {
        printToUser("Game Joined Successfully");
        BoardPrinter.printBoard(new ChessGame(), orientation);
    }

    @Override
    public String help() {
        return "";
    }

    @Override
    public EvalResult eval(String input) {
        return new EvalResult("", null);
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
