package chess;

import chess.moveCalculators.*;

import java.util.Collection;
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
        PieceMoveCalculator calculator = switch (designation) {
            case KING -> new KingMoveCalculator(board, myPosition);
            case QUEEN -> new QueenMoveCalculator(board, myPosition);
            case BISHOP -> new BishopMoveCalculator(board, myPosition);
            case ROOK -> new RookMoveCalculator(board, myPosition);
            case KNIGHT -> new KnightMoveCalculator(board, myPosition);
            case PAWN -> new PawnMoveCalculator(board, myPosition);
        };
        return calculator.getMoves();
    }

    @Override
    public String toString() {
        String type = switch (this.type) {
            case KING -> "k";
            case QUEEN -> "q";
            case BISHOP -> "b";
            case ROOK -> "r";
            case KNIGHT -> "n";
            case PAWN -> "p";
        };
        return pieceColor.equals(ChessGame.TeamColor.WHITE) ? type.toUpperCase() : type;
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
