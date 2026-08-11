package websocket.commands;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

public class MakeMoveCommand extends UserGameCommand {
    private final ChessMove move;

    public MakeMoveCommand(String authToken, Integer gameID, ChessPosition start, ChessPosition end, ChessPiece.PieceType promotionType) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = new ChessMove(start, end, promotionType);
    }

    public ChessMove getMove() {
        return move;
    }
}
