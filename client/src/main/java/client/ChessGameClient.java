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
        if (team == null || team.equals(WHITE)) {
            printBoard(false);
        } else {
            printBoard(true);
        }

    }

    private void printBoard(boolean flipped) {
        ChessPiece[][] board = new ChessGame().getBoard().getSquares();
        System.out.print(SET_TEXT_COLOR_BLACK);
        for (int row = 9 ; row >= 0 ; row--) {
            int orientedRow = flipped ? 9-row : row;
            for (int col = 0 ; col <= 9 ; col++) {
                int orientedCol = flipped ? 9-col : col;
                if (row == 0 || row == 9) {
                    System.out.print(SET_BG_COLOR_BLUE);
                    if (col == 0) {System.out.print("  ");}
                    else if (col == 9) {System.out.print("    ");}
                    else {
                        char columnName = (char) ('a' + orientedCol - 1);
                        System.out.print(" " + EMPTY + columnName);
                    }
                    continue;
                } else if (col == 0 || col == 9) {
                    System.out.print(SET_BG_COLOR_BLUE + String.format(" %s ", orientedRow));
                    continue;
                }

                if ((row + col) % 2 == 0) {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY);
                } else {
                    System.out.print(SET_BG_COLOR_WHITE);
                }

                ChessPiece occupant = board[orientedRow-1][orientedCol-1];
                if (occupant == null) {
                    System.out.printf(" %s ", EMPTY);
                } else {
                    String pieceChar = switch (occupant.getPieceType()) {
                        case KING -> occupant.getTeamColor().equals(WHITE) ? WHITE_KING : BLACK_KING;
                        case QUEEN -> occupant.getTeamColor().equals(WHITE) ? WHITE_QUEEN : BLACK_QUEEN;
                        case BISHOP -> occupant.getTeamColor().equals(WHITE) ? WHITE_BISHOP : BLACK_BISHOP;
                        case KNIGHT -> occupant.getTeamColor().equals(WHITE) ? WHITE_KNIGHT : BLACK_KNIGHT;
                        case ROOK -> occupant.getTeamColor().equals(WHITE) ? WHITE_ROOK : BLACK_ROOK;
                        case PAWN -> occupant.getTeamColor().equals(WHITE) ? WHITE_PAWN : BLACK_PAWN;
                    };
                    System.out.print(pieceChar);
                }
            }
            // end the line
            System.out.println(RESET_BG_COLOR);
        }
        // print own details
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
