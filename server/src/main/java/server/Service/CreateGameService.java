package server.Service;

import dataaccess.GameData;
import dataaccess.MemoryWriteData;

public class CreateGameService {
    public static Integer makeGame(String gameName) {
        GameData newGame = new GameData(gameName);
        MemoryWriteData.addGame(newGame);
        return newGame.getGameID();
    }
}
