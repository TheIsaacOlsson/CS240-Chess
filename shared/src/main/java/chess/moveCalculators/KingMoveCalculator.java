package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.PieceMoveCalculator;

import java.util.Collection;
import java.util.List;

public class KingMoveCalculator extends PieceMoveCalculator {

    private final int[][] relativeMovement = {
            {1, 0},
            {1, 1},
            {0, 1}
    };
    private final boolean anyDistance = false;
    private final boolean anyDirection = true;

    public KingMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> possibleMoves() {
        return List.of();
    }
}
