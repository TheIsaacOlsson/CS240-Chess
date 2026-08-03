package dataaccess;

import server.RequestResponse.JoinRequest;

public class MemoryWriteData implements WriteData {
    public static void addUser(UserData newUser) {
        MemoryDatabase.addUser(newUser);
    }

    public static void addAuth(AuthData newAuth) {
        MemoryDatabase.addAuth(newAuth);
    }

    public static void addGame(GameData newGame) {
        MemoryDatabase.addGame(newGame);}

    public static void addToGame(JoinRequest request, String username) {
        GameData requestedGame = MemoryDatabase.getGames().get(request.gameID());
        switch (request.playerColor()) {
            case "WHITE" -> requestedGame.whiteUsername = username;
            case "BLACK" -> requestedGame.blackUsername = username;
        }
    }

    public static void deleteAuth(String authToken) {
        MemoryDatabase.getCurrentAuth().remove(authToken);}

    public static void clearDatabase() {
        MemoryDatabase.clear();}
}
