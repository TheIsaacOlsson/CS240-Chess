package chess;

import chess.moveCalculators.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ChessPiece.PieceType designation = piece.getPieceType();
        switch (designation) {
            case KING -> {
                KingMoveCalculator calculator = new KingMoveCalculator(board, myPosition);
                return calculator.possibleMoves();
            }
            case QUEEN -> {
                QueenMoveCalculator calculator = new QueenMoveCalculator(board, myPosition);
                return calculator.possibleMoves();
            }
            case BISHOP -> {
                BishopMoveCalculator calculator = new BishopMoveCalculator(board, myPosition);
                return calculator.getMoves();
            }
            case ROOK -> {
                RookMoveCalculator calculator = new RookMoveCalculator(board, myPosition);
                return calculator.possibleMoves();
            }
            case KNIGHT -> {
                KnightMoveCalculator calculator = new KnightMoveCalculator(board, myPosition);
                return calculator.possibleMoves();
            }
            case PAWN -> {
                PawnMoveCalculator calculator = new PawnMoveCalculator(board, myPosition);
                return calculator.possibleMoves();
            }
            default -> {
                return List.of();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
