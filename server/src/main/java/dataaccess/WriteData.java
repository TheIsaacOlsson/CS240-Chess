package dataaccess;

import server.RequestResponse.JoinRequest;

public interface WriteData {
    void addUser(UserData newUser);
    void addAuth(AuthData newAuth);
    void addGame(GameData newGame);
    void addToGame(JoinRequest request, String username);
    void deleteAuth(String authToken);
    void clearDatabase();
}
