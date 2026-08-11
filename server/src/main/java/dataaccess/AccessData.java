package dataaccess;

import chess.ChessGame;
import serverFacade.ChessData.AuthData;
import serverFacade.ChessData.GameData;
import serverFacade.ChessData.UserData;
import serverFacade.RequestResponse.JoinRequest;

import java.util.Map;

public interface AccessData {
    UserData getUser(String username) throws ConnectionException;
    AuthData getAuthData(String authToken) throws ConnectionException;
    GameData getGameByID(Integer gameID) throws ConnectionException;
    Map<Integer, GameData> getGames() throws ConnectionException;
    void addUser(UserData newUser) throws ConnectionException;
    void addAuth(AuthData newAuth) throws ConnectionException;
    Integer addGame(GameData newGame) throws ConnectionException;
    void updateGame(GameData updatedGame) throws ConnectionException;
    void addToGame(JoinRequest request, String username) throws ConnectionException;
    void resetPlayer(Integer gameID, ChessGame.TeamColor team) throws ConnectionException;
    void deleteAuth(String authToken) throws ConnectionException;
    void clearDatabase() throws ConnectionException;
}
