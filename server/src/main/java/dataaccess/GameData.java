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

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    private Integer generateID() {
        while (Database.FetchData.getGames().containsKey(nextID)) {
            if(nextID == 9999) {nextID=1;} else {nextID++;}
        }
        return nextID++;
    }

    public int getGameID() {
        return gameID;
    }

    public AbbrGameData abbreviate() {
        return new AbbrGameData(this.getGameID(), this.whiteUsername, this.blackUsername, this.gameName);
    }
}
