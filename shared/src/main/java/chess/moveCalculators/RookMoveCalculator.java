package chess.moveCalculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalculator extends PieceMoveCalculator {

    private final int[][] relativeMovement= {
            {0, 1},
            {1, 0},
            {0, -1},
            {-1, 0},
    };

    public RookMoveCalculator(ChessBoard board, ChessPosition position) {
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
            for (int distance = 1; distance < 8; distance++) {
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
