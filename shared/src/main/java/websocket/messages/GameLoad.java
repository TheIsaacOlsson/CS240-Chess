package websocket.messages;

import chess.ChessGame;

public class GameLoad extends ServerMessage {
    public ChessGame gameState;

    public GameLoad(ServerMessageType type, ChessGame game) {
        super(type);
        assert type.equals(ServerMessageType.LOAD_GAME);

        gameState = game;
    }
}
