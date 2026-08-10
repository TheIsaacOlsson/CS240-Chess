package websocket.messages;

import chess.ChessGame;

public class GameLoad extends ServerMessage {
    public ChessGame game;

    public GameLoad(ServerMessageType type, ChessGame game) {
        super(type);
        assert type.equals(ServerMessageType.LOAD_GAME);

        this.game = game;
    }
}
