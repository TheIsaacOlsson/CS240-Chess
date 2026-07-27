package server.Service;

import dataaccess.GameData;
import dataaccess.MemoryFetchData;
import dataaccess.MemoryWriteData;
import server.RequestResponse.JoinRequest;

public class JoinGameService {
    public static void joinGame(JoinRequest request, String authToken) throws ColorTakenException {
        String user = MemoryFetchData.getAuthData(authToken).username();
        GameData requestedGame = MemoryFetchData.getGames().get(request.gameID());
        String currentPlayer = request.playerColor().equals("WHITE") ? requestedGame.getWhiteUsername() : requestedGame.getBlackUsername();
        if (currentPlayer != null) {
            throw new ColorTakenException("Error: already taken");
        }
        MemoryWriteData.addToGame(request, user);
    }
}
