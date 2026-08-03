package server.Service;

import dataaccess.Database;
import dataaccess.GameData;
import dataaccess.MemoryFetchData;
import dataaccess.MemoryWriteData;
import server.RequestResponse.JoinRequest;

public class JoinGameService {
    public static void joinGame(JoinRequest request, String authToken) throws ColorTakenException {
        String user = Database.FetchData.getAuthData(authToken).username();
        GameData requestedGame = Database.FetchData.getGames().get(request.gameID());
        String currentPlayer = request.playerColor().equals("WHITE") ? requestedGame.getWhiteUsername() : requestedGame.getBlackUsername();
        if (currentPlayer != null) {
            throw new ColorTakenException("Error: already taken");
        }
        Database.WriteData.addToGame(request, user);
    }
}
