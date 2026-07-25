package dataaccess;

import chess.ChessGame;
import server.RequestResponse.AbbrGameData;

public class GameData {
    private static Integer nextID = 1;

    final Integer gameID;
    String whiteUsername = null;
    String blackUsername = null;
    String gameName;
    ChessGame game = new ChessGame();

    public GameData(String gameName) {
        this.gameName = gameName;
        this.gameID = generateID();
    }

    private Integer generateID() {
        while (MemoryFetchData.getGames().containsKey(nextID)) {
            if(nextID == 9999) {nextID=1;} else {nextID++;}
        }
        return nextID++;
    }

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
