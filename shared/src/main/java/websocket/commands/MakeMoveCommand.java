package websocket.commands;

import chess.ChessPiece;
import chess.ChessPosition;

public class MakeMoveCommand extends UserGameCommand {
    private final ChessPosition start;
    private final ChessPosition end;
    private final ChessPiece.PieceType promotionType;

    public MakeMoveCommand(String authToken, Integer gameID, ChessPosition start, ChessPosition end, ChessPiece.PieceType promotionType) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.start = start;
        this.end = end;
        this.promotionType = promotionType;
    }

    public ChessPosition getStart() {
        return start;
    }

    public ChessPosition getEnd() {
        return end;
    }

    public ChessPiece.PieceType getPromotionType() {return promotionType;}
}
