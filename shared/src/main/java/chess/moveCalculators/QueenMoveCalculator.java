package chess.moveCalculators;

import chess.*;

import java.util.Collection;

public class QueenMoveCalculator extends PieceMoveCalculator {

    private final static int[][] relativeMovement = {
            {0, 1},
            {1, 1},
            {1, 0},
            {1, -1},
            {0, -1},
            {-1, -1},
            {-1, 0},
            {-1, 1}
    };
    private final Collection<ChessMove> moves;

    public QueenMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
        moves = super.possibleMoves(relativeMovement, 8);
    }

    @Override
    public Collection<ChessMove> getMoves() {
        return moves;
    }
}