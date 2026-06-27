package chess.moveCalculators;

import chess.*;

import java.util.Collection;

public class KnightMoveCalculator extends PieceMoveCalculator {

    private final static int[][] relativeMovement= {
            {1, 2},
            {2, 1},
            {2, -1},
            {1, -2},
            {-1, -2},
            {-2, -1},
            {-2, 1},
            {-1, 2}
    };
    private final Collection<ChessMove> moves;

    public KnightMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
        moves = super.possibleMoves(relativeMovement, 1);
    }

    @Override
    public Collection<ChessMove> getMoves() {
        return moves;
    }
}
