package chess;

import java.util.Collection;
import java.util.List;

public class PieceMoveCalculator {

    private final ChessBoard board;
    private final ChessPosition position;

    public PieceMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> possibleMoves() {
        return List.of();
    }
}
