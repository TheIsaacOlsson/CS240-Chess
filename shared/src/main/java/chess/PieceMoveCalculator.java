package chess;

import chess.moveCalculators.KingMoveCalculator;

import java.util.Collection;
import java.util.List;

public class PieceMoveCalculator {

    private final ChessBoard board;
    private final ChessPosition position;

    public PieceMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public ChessPosition getPosition() {
        return position;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public Collection<ChessMove> possibleMoves() {
        ChessPiece piece = board.getPiece(position);
        ChessPiece.PieceType designation = piece.getPieceType();
        switch (designation) {
            case KING -> {
                KingMoveCalculator calculator = new KingMoveCalculator(board, position);
                return calculator.possibleMoves();
            }
            default -> {
                return List.of();
            }
        }
    }
}
