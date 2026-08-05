package dataaccess;

import server.RequestResponse.JoinRequest;

import java.util.Map;

public interface AccessData {
    UserData getUser(String username);
    AuthData getAuthData(String authToken);
    GameData getGameByID(Integer gameID);
    Map<Integer, GameData> getGames();
    void addUser(UserData newUser);
    void addAuth(AuthData newAuth);
    void addGame(GameData newGame);
    void addToGame(JoinRequest request, String username);
    void deleteAuth(String authToken);
    void clearDatabase();
}
