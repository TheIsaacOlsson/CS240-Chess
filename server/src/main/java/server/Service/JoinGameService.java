package server.Service;

import chess.ChessGame;
import dataaccess.ConnectionException;
import dataaccess.DataAccessException;
import dataaccess.Database;
import serverFacade.ChessData.GameData;
import serverFacade.RequestResponse.JoinRequest;

public class JoinGameService {
    public static void joinGame(JoinRequest request, String authToken) throws ColorTakenException, ConnectionException {
        String user = Database.DataAccess.getAuthData(authToken).username();
        GameData requestedGame = Database.DataAccess.getGameByID(request.gameID());
        if (requestedGame == null) { throw new ConnectionException("Game does not exist"); }
        String currentPlayer = request.playerColor().equals(ChessGame.TeamColor.WHITE) ? requestedGame.getWhiteUsername() : requestedGame.getBlackUsername();
        if (currentPlayer != null) {
            if (currentPlayer.equals(user)) { return; }
            else { throw new ColorTakenException("Error: already taken"); }
        }
        Database.DataAccess.addToGame(request, user);
    }
}
