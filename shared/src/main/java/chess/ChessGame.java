package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard gameBoard;
    private ChessPosition whiteKingPosition;
    private ChessPosition blackKingPosition;

    public ChessGame() {
        turn = TeamColor.WHITE;
        gameBoard = new ChessBoard();
        gameBoard.resetBoard();
        whiteKingPosition = new ChessPosition(1, 5);
        blackKingPosition = new ChessPosition(8, 5);
    }

    public ChessGame(ChessGame game) {
        this.turn = game.getTeamTurn();
        setBoard(game.getBoard());
        this.whiteKingPosition = game.getKingPosition(TeamColor.WHITE);
        this.blackKingPosition = game.getKingPosition(TeamColor.BLACK);
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

    public ChessPosition getKingPosition(TeamColor color) {
        return color.equals(TeamColor.WHITE) ? whiteKingPosition : blackKingPosition;
    }

    public void setKingPosition(TeamColor color, ChessPosition newPosition) {
        if (color.equals(TeamColor.WHITE)) {
            whiteKingPosition = newPosition;
        } else {
            blackKingPosition = newPosition;
        }
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

        ChessGame sandbox = new ChessGame(this);
        sandbox.setTeamTurn(piece.getTeamColor());

        for (ChessMove move : providedMoves) {
            try{
                MoveRecord moveInfo = sandbox.makeMove(move);

                validMoves.add(move);
                sandbox.undoMove(moveInfo);
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
     * @return Record of information needed to undo the move
     */
    public MoveRecord makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startSquare = move.getStartPosition();
        ChessPiece movingPiece = gameBoard.getPiece(startSquare);
        if (movingPiece == null) {throw new InvalidMoveException("No piece at start position");}

        TeamColor team = movingPiece.getTeamColor();
        if ( ! team.equals(getTeamTurn())) {throw new InvalidMoveException("Wrong turn");}

        Collection<ChessMove> pieceMoves = movingPiece.pieceMoves(gameBoard, startSquare);
        if ( ! pieceMoves.contains(move)) {throw new InvalidMoveException("Invalid movement");}

        ChessPosition targetSquare = move.getEndPosition();
        ChessPiece occupant = gameBoard.getPiece(targetSquare);
        if (occupant!=null && team.equals(occupant.getTeamColor())) {throw new InvalidMoveException("Occupied target position");}

        ChessPiece.PieceType pieceType = movingPiece.getPieceType();

        MoveRecord newMove = new MoveRecord(move, pieceType, occupant);
        gameBoard.movePiece(move);
        turn = turn.equals(TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE ;
        if (pieceType.equals(ChessPiece.PieceType.KING)) {setKingPosition(team, move.getEndPosition());}

        if (isInCheck(team)) {
            undoMove(newMove);
            throw new InvalidMoveException("Cannot willingly put/keep your King in check.");
        } else {
            return newMove;
        }
    }

    /**
     * Records the information needed to fully undo a move and restore pieces
     *
     * @param move The move that was made
     * @param originalType The type of piece it was before the move
     * @param capturedPiece The piece that was captured during the move
     */
    public record MoveRecord(ChessMove move, ChessPiece.PieceType originalType, ChessPiece capturedPiece) {}

    /**
     * Reverses the changes caused by a move. Returns the piece to its original location and restores captured pieces.
     *
     * @param moveInfo Record object with the move to undo, original piece type, and the piece that was captured.
     */
    private void undoMove(MoveRecord moveInfo) {
        gameBoard.movePiece(new ChessMove(moveInfo.move.getEndPosition(), moveInfo.move.getStartPosition(), moveInfo.originalType));
        if (moveInfo.capturedPiece != null) {gameBoard.addPiece(moveInfo.move.getEndPosition(), moveInfo.capturedPiece);}
        turn = turn.equals(TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE ;
        if (moveInfo.originalType.equals(ChessPiece.PieceType.KING)) {setKingPosition(turn, moveInfo.move.getStartPosition());}
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = (teamColor.equals(TeamColor.WHITE)) ? whiteKingPosition : blackKingPosition;

        for (ChessPosition occupiedPosition : gameBoard.getOccupiedPositions()) {
            ChessPiece occupant = gameBoard.getPiece(occupiedPosition);
            if (occupant.getTeamColor().equals(teamColor)) {continue;}
            Collection<ChessMove> moves = occupant.pieceMoves(gameBoard, occupiedPosition);

            for (ChessMove possibleCapture : moves) {
                if (possibleCapture.getEndPosition().equals(kingPosition)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if ( ! isInCheck(teamColor)) {return false;}

        for (ChessPosition occupiedPosition : gameBoard.getOccupiedPositions()) {
            ChessPiece occupant = gameBoard.getPiece(occupiedPosition);
            if ( ! occupant.getTeamColor().equals(teamColor)) {continue;}

            if ( ! validMoves(occupiedPosition).isEmpty()) {return false;}
        }

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        for (ChessPosition position : gameBoard.getOccupiedPositions()) {
            TeamColor team = gameBoard.getPiece(position).getTeamColor();
            if ( ! team.equals(teamColor)) {continue;}

            Collection<ChessMove> possibleMoves = validMoves(position);
            if ( ! possibleMoves.isEmpty()) {return false;}
        }
        return true;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = new ChessBoard(board);
        for (ChessPosition position : board.getOccupiedPositions()) {
            ChessPiece occupant = gameBoard.getPiece(position);
            if (occupant.getPieceType().equals(ChessPiece.PieceType.KING)) {
                if (occupant.getTeamColor().equals(TeamColor.WHITE)) {
                    whiteKingPosition = position;
                } else {
                    blackKingPosition = position;
                }
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(gameBoard, chessGame.gameBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, gameBoard);
    }
}
