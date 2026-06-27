package chess;

import chess.moveCalculators.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PieceMoveCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    private final Collection<ChessMove> moves = List.of();

    public PieceMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> getMoves() {
        return moves;
    }

    public ChessPosition getPosition() {
        return position;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public Collection<ChessMove> possibleMoves(int[][] relativeMovement, int movementRange) {
        ChessPiece myself = board.getPiece(position);
        ArrayList<ChessMove> moves = new ArrayList<>();
        int startRow = position.getRow();
        int startCol = position.getColumn();
        for (int[] move : relativeMovement) {
            for (int distance = 1; distance <= movementRange; distance++) { // For movementRange==1, will only run once
                int endRow = startRow + move[0]*distance;
                int endCol = startCol + move[1]*distance;
                if (endRow < 1 || endRow > 8 || endCol < 1 || endCol > 8) {
                    break;
                } else {
                    ChessPosition endPosition = new ChessPosition(endRow, endCol);
                    ChessPiece occupant = board.getPiece(endPosition);
                    if (occupant == null) {
                        moves.add(new ChessMove(position, endPosition, null));
                    } else if (occupant.getTeamColor().equals(myself.getTeamColor())) {
                        break;
                    } else {
                        moves.add(new ChessMove(position, endPosition, null));
                        break;
                    }
                }
            }
        }

        return moves;
    }
}
