package dataaccess;

import server.RequestResponse.JoinRequest;

public class SQLWriteData implements WriteData{
    public void addUser(UserData newUser) {}
    public void addAuth(AuthData newAuth) {}
    public void addGame(GameData newGame) {}
    public void addToGame(JoinRequest request, String username) {}
    public void deleteAuth(String authToken) {}
    public void clearDatabase() {}
}
