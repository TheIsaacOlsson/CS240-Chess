package dataaccess;

import server.RequestResponse.JoinRequest;

import java.util.Map;

public class MemoryAccessData implements AccessData {
    public void addUser(UserData newUser) {
        MemoryDatabase.addUser(newUser);
    }

    public void addAuth(AuthData newAuth) {
        MemoryDatabase.addAuth(newAuth);
    }

    public void addGame(GameData newGame) {
        MemoryDatabase.addGame(newGame);
    }

    public UserData getUser(String username) {
        return MemoryDatabase.getUsers().get(username);
    }

    public AuthData getAuthData(String authToken) {return MemoryDatabase.getCurrentAuth().get(authToken);}

    public GameData getGameByID(Integer gameID) {return MemoryDatabase.getGames().get(gameID);}

    public Map<Integer, GameData> getGames() {return MemoryDatabase.getGames();}

    public void addToGame(JoinRequest request, String username) {
        GameData requestedGame = MemoryDatabase.getGames().get(request.gameID());
        switch (request.playerColor()) {
            case WHITE -> requestedGame.whiteUsername = username;
            case BLACK -> requestedGame.blackUsername = username;
        }
    }

    public void deleteAuth(String authToken) {
        MemoryDatabase.getCurrentAuth().remove(authToken);}

    public void clearDatabase() {
        MemoryDatabase.clear();}
}
