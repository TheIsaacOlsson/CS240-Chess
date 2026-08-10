package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.Set;

import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class BoardPrinter {
    public static String printBoard(ChessGame game, Set<ChessPosition> highlightedSquares, boolean flipped) {
        try {
            ChessBoard board = game.getBoard();
            boolean highlight = false;
            StringBuilder output = new StringBuilder();
            output.append("\n");
            output.append(SET_TEXT_COLOR_BLACK);
            for (int row = 9 ; row >= 0 ; row--) {
                int orientedRow = flipped ? 9-row : row;
                for (int col = 0 ; col <= 9 ; col++) {
                    int orientedCol = flipped ? 9-col : col;
                    if (row == 0 || row == 9) {
                        output.append(SET_BG_COLOR_BLUE);
                        if (col == 0) {output.append("  ");}
                        else if (col == 9) {output.append("    ");}
                        else {
                            char columnName = (char) ('a' + orientedCol - 1);
                            output.append(" " + EMPTY + columnName);
                        }
                        continue;
                    } else if (col == 0 || col == 9) {
                        output.append(SET_BG_COLOR_BLUE + String.format(" %s ", orientedRow));
                        continue;
                    }

                    ChessPosition position = new ChessPosition(orientedRow, orientedCol);
                    if (highlightedSquares != null && highlightedSquares.contains(position)) {
                        highlight = true;
                    }

                    if ((row + col) % 2 == 0) {
                        output.append(highlight ? SET_BG_COLOR_DARK_GREEN : SET_BG_COLOR_LIGHT_GREY);
                    } else {
                        output.append(highlight ? SET_BG_COLOR_GREEN : SET_BG_COLOR_WHITE);
                    }

                    ChessPiece occupant = board.getPiece(position);
                    if (occupant == null) {
                        output.append(String.format(" %s ", EMPTY));
                    } else {
                        String pieceChar = switch (occupant.getPieceType()) {
                            case KING -> occupant.getTeamColor().equals(WHITE) ? WHITE_KING : BLACK_KING;
                            case QUEEN -> occupant.getTeamColor().equals(WHITE) ? WHITE_QUEEN : BLACK_QUEEN;
                            case BISHOP -> occupant.getTeamColor().equals(WHITE) ? WHITE_BISHOP : BLACK_BISHOP;
                            case KNIGHT -> occupant.getTeamColor().equals(WHITE) ? WHITE_KNIGHT : BLACK_KNIGHT;
                            case ROOK -> occupant.getTeamColor().equals(WHITE) ? WHITE_ROOK : BLACK_ROOK;
                            case PAWN -> occupant.getTeamColor().equals(WHITE) ? WHITE_PAWN : BLACK_PAWN;
                        };
                        output.append(pieceChar);
                    }
                    highlight = false;
                }
                // end the line
                output.append(RESET_BG_COLOR + "\n");
            }
            return output.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
