package chess.moveCalculators;

import chess.*;

import java.util.Collection;

public class KingMoveCalculator extends PieceMoveCalculator {

    private final static int[][] relativeMovement= {
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

    public KingMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
        moves = super.possibleMoves(relativeMovement, 1);
    }

    @Override
    public Collection<ChessMove> getMoves() {
        return moves;
    }
}
