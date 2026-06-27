package chess.moveCalculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMoveCalculator extends PieceMoveCalculator {

    private final int[][] relativeMovement= {
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

    public Collection<ChessMove> getMoves() {
        return moves;
    }

}
