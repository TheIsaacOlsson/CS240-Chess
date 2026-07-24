package dataaccess;

import chess.ChessGame;

public class GameData {
    private int gameID;
    String whiteUsername;
    String blackUsername;
    String gameName;
    ChessGame game;

    public int getGameID() {
        return gameID;
    }
}
