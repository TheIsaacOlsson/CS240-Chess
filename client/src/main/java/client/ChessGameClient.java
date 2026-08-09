package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.Map;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class ChessGameClient implements Client {
    private final ServerFacade server;
    private final Scanner scanner;
    private Map<String, Object> clientData;

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
    }

    @Override
    public void run() {
        printToUser("Game Joined Successfully");
        ChessGame.TeamColor team = (ChessGame.TeamColor) clientData.get("playerColor");
        BoardPrinter.printBoard(new ChessGame(), team != null && team.equals(BLACK));
    }

    @Override
    public String help() {
        return "";
    }

    @Override
    public EvalResult eval(String input) {
        return new EvalResult("", null);
    }
}
