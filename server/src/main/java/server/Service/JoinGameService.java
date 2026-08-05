package server.Service;

import chess.ChessGame;
import dataaccess.Database;
import dataaccess.GameData;
import server.RequestResponse.JoinRequest;

public class JoinGameService {
    public static void joinGame(JoinRequest request, String authToken) throws ColorTakenException {
        String user = Database.DataAccess.getAuthData(authToken).username();
        GameData requestedGame = Database.DataAccess.getGameByID(request.gameID());
        String currentPlayer = request.playerColor().equals(ChessGame.TeamColor.WHITE) ? requestedGame.getWhiteUsername() : requestedGame.getBlackUsername();
        if (currentPlayer != null) {
            throw new ColorTakenException("Error: already taken");
        }
        Database.DataAccess.addToGame(request, user);
    }
}
