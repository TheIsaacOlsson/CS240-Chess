package server.Service;

import dataaccess.Database;
import dataaccess.GameData;

public class CreateGameService {
    public static Integer makeGame(String gameName) {
        GameData newGame = new GameData(gameName);
        Database.DataAccess.addGame(newGame);
        return newGame.getGameID();
    }
}
