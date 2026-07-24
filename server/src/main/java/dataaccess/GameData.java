package dataaccess;

import chess.ChessGame;
import server.RequestResponse.AbbrGameData;

public class GameData {
    private int gameID;
    String whiteUsername;
    String blackUsername;
    String gameName;
    ChessGame game;

    public int getGameID() {
        return gameID;
    }

    public AbbrGameData abbreviate() {
        return new AbbrGameData(this.getGameID(), nonNull(this.whiteUsername), nonNull(this.blackUsername), this.gameName);
    }

    private static String nonNull(String name) {
        return name == null ? "none" : name;
    }
}
