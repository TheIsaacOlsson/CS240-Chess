package server.Service;

import chess.ChessGame;
import chess.InvalidMoveException;
import dataaccess.ConnectionException;
import dataaccess.DataAccessException;
import dataaccess.Database;
import serverFacade.ChessData.AuthData;
import serverFacade.ChessData.GameData;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class ResignationService {
    public static void resignGame(Integer gameID, String authToken) throws DataAccessException {
        try {
            String user = GetAuth.getAuth(authToken).username();
            GameData gameData = GetGameData.getGameByID(gameID);
            ChessGame game = gameData.getGame();
            if (user.equals(gameData.getWhiteUsername())) {game.resign(WHITE);}
            else if (user.equals(gameData.getBlackUsername())) {game.resign(BLACK);}
            else {throw new DataAccessException("Cannot resign");}
            Database.DataAccess.updateGame(gameData);
        } catch (ConnectionException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
