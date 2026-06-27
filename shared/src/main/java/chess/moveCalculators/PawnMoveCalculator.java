package chess.moveCalculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator extends PieceMoveCalculator {

    private final int[][] relativeMovement= {
            {0, 1}
    };
    private final int[][] captureMovement= {
            {1, 1},
            {1, -1}
    };

    public PawnMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> possibleMoves() {
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
                    moves.add(new ChessMove(position, endPosition, null));
                }
            }
        }

        if (startRow+orientation >= 1 && startRow+orientation <= 8) {
            ChessPosition endPosition = new ChessPosition(startRow+orientation, startCol);
            ChessPiece occupant = board.getPiece(endPosition);
            if (occupant == null) {
                moves.add(new ChessMove(position, endPosition, null));

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
}
