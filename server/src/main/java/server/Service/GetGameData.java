package server.Service;

import dataaccess.ConnectionException;
import dataaccess.Database;
import serverFacade.ChessData.GameData;

public class GetGameData {
    public static GameData getGameByID(Integer gameID) throws ConnectionException {
        return Database.DataAccess.getGameByID(gameID);
    }
}
