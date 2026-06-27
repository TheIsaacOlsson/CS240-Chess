package chess;

import chess.moveCalculators.*;

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
            case QUEEN -> {
                QueenMoveCalculator calculator = new QueenMoveCalculator(board, position);
                return calculator.possibleMoves();
            }
            case BISHOP -> {
                BishopMoveCalculator calculator = new BishopMoveCalculator(board, position);
                return calculator.possibleMoves();
            }
            case ROOK -> {
                RookMoveCalculator calculator = new RookMoveCalculator(board, position);
                return calculator.possibleMoves();
            }
            case KNIGHT -> {
                KnightMoveCalculator calculator = new KnightMoveCalculator(board, position);
                return calculator.possibleMoves();
            }
            case PAWN -> {
                PawnMoveCalculator calculator = new PawnMoveCalculator(board, position);
                return calculator.possibleMoves();
            }
            default -> {
                return List.of();
            }
        }
    }
}
