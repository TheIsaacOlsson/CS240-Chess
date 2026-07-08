package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard gameBoard;

    public ChessGame() {
        turn = TeamColor.WHITE;
        gameBoard = new ChessBoard();
        gameBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = gameBoard.getPiece(startPosition);

        Collection<ChessMove> providedMoves = piece.pieceMoves(gameBoard, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();

        for (ChessMove move : providedMoves) {
            try{
                testMove(move);

                validMoves.add(move);

            } catch (InvalidMoveException e) {
                System.out.println(e.getMessage());
            }
        }

        return validMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        try {
            testMove(move);
        } catch (InvalidMoveException moveException) {
            throw moveException;
        }

        gameBoard.movePiece(move);
    }

    /**
     * Checks if a move is valid.
     *
     * @param move The move being checked
     * @throws InvalidMoveException when the given move cannot/should not be completed
     */
    private void testMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startSquare = move.getStartPosition();
        ChessPiece movingPiece = gameBoard.getPiece(startSquare);
        if (movingPiece == null) {throw new InvalidMoveException("No piece at start position");}

        Collection<ChessMove> pieceMoves = movingPiece.pieceMoves(gameBoard, startSquare);
        if ( ! pieceMoves.contains(move)) {throw new InvalidMoveException("Invalid movement");}

        ChessPosition targetSquare = move.getEndPosition();
        ChessPiece occupant = gameBoard.getPiece(targetSquare);
        if (movingPiece.getTeamColor().equals(occupant.getTeamColor())) {throw new InvalidMoveException("Occupied target position");}
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }
}
