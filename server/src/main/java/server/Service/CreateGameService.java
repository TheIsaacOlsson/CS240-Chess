package server.Service;

import dataaccess.ConnectionException;
import dataaccess.Database;
import dataaccess.GameData;

public class CreateGameService {
    public static Integer makeGame(String gameName) throws ConnectionException {
        GameData newGame = new GameData(gameName);
        Database.DataAccess.addGame(newGame);
        return newGame.getGameID();
    }
}
