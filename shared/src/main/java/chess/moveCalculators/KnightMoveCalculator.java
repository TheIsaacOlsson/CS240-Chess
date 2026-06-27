package chess.moveCalculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMoveCalculator extends PieceMoveCalculator {

    private final int[][] relativeMovement= {
            {1, 2},
            {2, 1},
            {2, -1},
            {1, -2},
            {-1, -2},
            {-2, -1},
            {-2, 1},
            {-1, 2}
    };

    public KnightMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> possibleMoves() {
        ChessBoard board = super.getBoard();
        ChessPosition position = super.getPosition();
        ChessPiece myself = board.getPiece(position);
        ArrayList<ChessMove> moves = new ArrayList<>();
        int startRow = position.getRow();
        int startCol = position.getColumn();
        for (int[] move : relativeMovement) {
            int endRow = startRow + move[0];
            int endCol = startCol + move[1];
            if (endRow < 1 || endRow > 8 || endCol < 1 || endCol > 8) {
                continue;
            } else {
                ChessPosition endPosition = new ChessPosition(endRow, endCol);
                ChessPiece occupant = board.getPiece(endPosition);
                if (occupant == null || !(occupant.getTeamColor().equals(myself.getTeamColor()))) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
            }
        }

        return moves;
    }
}
