package server.Services;

import dataaccess.GameData;
import dataaccess.MemoryFetchData;
import server.RequestResponse.AbbrGameData;

import java.util.ArrayList;

public class GetGamesService {
    public static AbbrGameData[] representGames() {
        GameData[] allGameData = MemoryFetchData.getGames();
        ArrayList<AbbrGameData> abbreviatedGames = new ArrayList<>();
        for (GameData game : allGameData) {
            abbreviatedGames.add(game.abbreviate());
        }
        return abbreviatedGames.toArray(new AbbrGameData[]{});
    }
}
