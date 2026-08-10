package websocket.messages;

import chess.ChessGame;
import chess.ChessPosition;

import java.util.Set;

public class GameLoad extends ServerMessage {
    public ChessGame game;
    public Set<ChessPosition> highlightSquares;

    public GameLoad(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);

        this.game = game;
    }

    public GameLoad(ChessGame game, Set<ChessPosition> squares) {
        super(ServerMessageType.LOAD_GAME);
        highlightSquares = squares;
    }
}
