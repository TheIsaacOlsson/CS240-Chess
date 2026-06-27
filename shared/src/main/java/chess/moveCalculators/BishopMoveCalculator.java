package chess.moveCalculators;

import chess.*;

import java.util.Collection;

public class BishopMoveCalculator extends PieceMoveCalculator {

    private final static int[][] relativeMovement= {
            {1, 1},
            {1, -1},
            {-1, -1},
            {-1, 1}
    };
    private final Collection<ChessMove> moves;

    public BishopMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
        moves = super.possibleMoves(relativeMovement, 8);
    }

    @Override
    public Collection<ChessMove> getMoves() {
        return moves;
    }

}
