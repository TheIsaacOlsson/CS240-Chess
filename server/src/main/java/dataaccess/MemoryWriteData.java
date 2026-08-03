package dataaccess;

import server.RequestResponse.JoinRequest;

public class MemoryWriteData implements WriteData {
    public void addUser(UserData newUser) {
        MemoryDatabase.addUser(newUser);
    }

    public void addAuth(AuthData newAuth) {
        MemoryDatabase.addAuth(newAuth);
    }

    public void addGame(GameData newGame) {
        MemoryDatabase.addGame(newGame);
    }

    public void addToGame(JoinRequest request, String username) {
        GameData requestedGame = MemoryDatabase.getGames().get(request.gameID());
        switch (request.playerColor()) {
            case "WHITE" -> requestedGame.whiteUsername = username;
            case "BLACK" -> requestedGame.blackUsername = username;
        }
    }

    public void deleteAuth(String authToken) {
        MemoryDatabase.getCurrentAuth().remove(authToken);}

    public void clearDatabase() {
        MemoryDatabase.clear();}
}
