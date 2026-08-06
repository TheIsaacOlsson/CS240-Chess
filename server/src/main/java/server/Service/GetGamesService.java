package server.Service;

import dataaccess.ConnectionException;
import dataaccess.Database;
import serverFacade.ChessData.GameData;
import serverFacade.RequestResponse.AbbrGameData;

import java.util.ArrayList;
import java.util.Map;

public class GetGamesService {
    public static AbbrGameData[] representGames() throws ConnectionException {
        Map<Integer, GameData> allGameData = Database.DataAccess.getGames();
        ArrayList<AbbrGameData> abbreviatedGames = new ArrayList<>();
        for (GameData game : allGameData.values()) {
            abbreviatedGames.add(game.abbreviate());
        }
        return abbreviatedGames.toArray(new AbbrGameData[]{});
    }
}
