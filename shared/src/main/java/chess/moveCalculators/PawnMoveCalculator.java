package chess.moveCalculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMoveCalculator extends PieceMoveCalculator {

    private final static int[][] relativeMovement= {
            {0, 1}
    };
    private final static int[][] captureMovement= {
            {1, 1},
            {1, -1}
    };
    private final Collection<ChessMove> moves;

    public PawnMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
        moves = possibleMoves(relativeMovement, 8);
    }

    @Override
    public Collection<ChessMove> getMoves() {
        return moves;
    }

    @Override
    public Collection<ChessMove> possibleMoves(int[][] relativeMovement, int range) {
        ChessBoard board = super.getBoard();
        ChessPosition position = super.getPosition();
        ChessPiece myself = board.getPiece(position);
        byte orientation = myself.getTeamColor().equals(ChessGame.TeamColor.WHITE) ? (byte) 1 : (byte) -1;
        ArrayList<ChessMove> moves = new ArrayList<>();
        int startRow = position.getRow();
        int startCol = position.getColumn();
        for (int[] capture : captureMovement) {
            int endRow = startRow + orientation;
            int endCol = startCol + capture[1];
            if (endRow < 1 || endRow > 8 || endCol < 1 || endCol > 8) {
                continue;
            } else {
                ChessPosition endPosition = new ChessPosition(endRow, endCol);
                ChessPiece occupant = board.getPiece(endPosition);
                if (!(occupant == null) && !(occupant.getTeamColor().equals(myself.getTeamColor()))) {
                    moves.addAll(possiblePromotions(position, endPosition, orientation));
                }
            }
        }

        if (startRow+orientation >= 1 && startRow+orientation <= 8) {
            ChessPosition endPosition = new ChessPosition(startRow+orientation, startCol);
            ChessPiece occupant = board.getPiece(endPosition);
            if (occupant == null) {
                moves.addAll(possiblePromotions(position, endPosition, orientation));

                if (orientation == 1 && startRow == 2 || orientation == -1 && startRow == 7) {
                    endPosition = new ChessPosition(startRow+2*orientation, startCol);
                    occupant = board.getPiece(endPosition);
                    if (occupant == null) {
                        moves.add(new ChessMove(position, endPosition, null));
                    }
                }
            }
        }

        return moves;
    }

    private Collection<ChessMove> possiblePromotions(ChessPosition startPosition, ChessPosition endPosition, byte orientation) {
        if (orientation==1 && endPosition.getRow() == 8 || orientation==-1 && endPosition.getRow() == 1) {
            ArrayList<ChessMove> allPromotions = new ArrayList<>();
            for (ChessPiece.PieceType type : chess.ChessPiece.PieceType.values()) {
                if (type.equals(ChessPiece.PieceType.PAWN) || type.equals(ChessPiece.PieceType.KING)) {continue;}
                allPromotions.add(new ChessMove(startPosition, endPosition, type));
            }
            return allPromotions;
        } else {
            return List.of(new ChessMove(startPosition, endPosition, null));
        }
    }
}
