package chess;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares = new ChessPiece[8][8];
    private HashSet<ChessPosition> occupiedPositions = new HashSet<>();

    public ChessBoard() {

    }

    public ChessBoard(ChessBoard copy) {
        for (ChessPosition position : copy.getOccupiedPositions()) {
            this.addPiece(position, copy.getPiece(position));
        }
    }

    /**
     * Returns the set of occupied positions
     */
    public HashSet<ChessPosition> getOccupiedPositions() {
        return occupiedPositions;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
        occupiedPositions.add(position);
    }

    /**
     * Removes a piece from the board at the given location
     *
     * @param position the position for which to delete the occupying piece.
     */
    public void deletePiece(ChessPosition position) {
        squares[position.getRow()-1][position.getColumn()-1] = null;
        occupiedPositions.remove(position);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Moves a piece from one location to another, removing any occupying piece if necessary.
     *
     * @param move The movement being executed. DOES NOT REQUIRE A VALID MOVE.
     */
    public void movePiece(ChessMove move) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece movingPiece = getPiece(startPosition);
        deletePiece(endPosition);
        deletePiece(startPosition);
        ChessPiece.PieceType prestigeType = (move.getPromotionPiece() == null) ? movingPiece.getPieceType() : move.getPromotionPiece();
        addPiece(endPosition, new ChessPiece(movingPiece.getTeamColor(), prestigeType));
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //Delete current occupants
        for (ChessPosition position : occupiedPositions) {
            squares[position.getRow()-1][position.getColumn()-1] = null;
        }
        occupiedPositions.clear();

        //Place pieces in their starting location
        int[] rookColumns = {1, 8};
        for (int column : rookColumns) {
            addPiece(new ChessPosition(1, column), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
            addPiece(new ChessPosition(8, column), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        }
        int[] knightColumns = {2, 7};
        for (int column : knightColumns) {
            addPiece(new ChessPosition(1, column), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
            addPiece(new ChessPosition(8, column), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        }
        int[] bishopColumns = {3, 6};
        for (int column : bishopColumns) {
            addPiece(new ChessPosition(1, column), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
            addPiece(new ChessPosition(8, column), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        }
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));

        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));

        for (int col=1 ; col<=8 ; col++) {
            addPiece(new ChessPosition(2, col), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, col), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (ChessPiece[] row : squares) {
            for (ChessPiece piece : row) {
                builder.append('|');
                if (piece==null) {
                    builder.append(' ');
                } else {
                    builder.append(piece);
                }
            }
            builder.append("|\n");
        }
        /*
        for (int i=1; i <= 8 ; i++) {
            for (int j=1; j <= 8 ; j++) {
                builder.append('|');
                if (occupiedPositions.contains(new ChessPosition(i, j))) {
                    builder.append('x');
                } else {
                    builder.append(' ');
                }
            }
            builder.append("|\n");
        }
        */
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}
