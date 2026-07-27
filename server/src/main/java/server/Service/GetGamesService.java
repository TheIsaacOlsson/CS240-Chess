package server.Service;

import dataaccess.GameData;
import dataaccess.MemoryFetchData;
import server.RequestResponse.AbbrGameData;

import java.util.ArrayList;
import java.util.Map;

public class GetGamesService {
    public static AbbrGameData[] representGames() {
        Map<Integer, GameData> allGameData = MemoryFetchData.getGames();
        ArrayList<AbbrGameData> abbreviatedGames = new ArrayList<>();
        for (GameData game : allGameData.values()) {
            abbreviatedGames.add(game.abbreviate());
        }
        return abbreviatedGames.toArray(new AbbrGameData[]{});
    }
}
