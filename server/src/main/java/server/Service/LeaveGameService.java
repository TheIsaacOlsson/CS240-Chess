package server.Service;

import dataaccess.ConnectionException;
import dataaccess.Database;
import serverFacade.ChessData.AuthData;
import serverFacade.ChessData.GameData;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class LeaveGameService {
    public static void leaveGame(String authToken, Integer gameID) throws ConnectionException {
        AuthData auth = GetAuth.getAuth(authToken);
        if (auth == null) {
            throw new ConnectionException("Unauthorized");
        }
        String username = auth.username();
        GameData gameData = GetGameData.getGameByID(gameID);
        if (username.equals(gameData.getWhiteUsername())) {
            Database.DataAccess.resetPlayer(gameID, WHITE);
        } else if (username.equals(gameData.getBlackUsername())) {
            Database.DataAccess.resetPlayer(gameID, BLACK);
        }
    }
}
