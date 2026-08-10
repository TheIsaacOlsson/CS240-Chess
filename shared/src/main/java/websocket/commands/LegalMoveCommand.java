package websocket.commands;

import chess.ChessPosition;

public class LegalMoveCommand extends UserGameCommand {
    private ChessPosition position;

    public LegalMoveCommand(String authToken, Integer gameID, ChessPosition position) {
        super(CommandType.LEGAL, authToken, gameID);
        this.position = position;
    }

    public ChessPosition getPosition() {
        return position;
    }
}
