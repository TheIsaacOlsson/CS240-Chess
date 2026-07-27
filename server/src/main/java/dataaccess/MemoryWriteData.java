package dataaccess;

import server.RequestResponse.JoinRequest;

public class MemoryWriteData implements WriteData {
    public static void addUser(UserData newUser) {
        Database.addUser(newUser);
    }

    public static void addAuth(AuthData newAuth) {
        Database.addAuth(newAuth);
    }

    public static void addGame(GameData newGame) {Database.addGame(newGame);}

    public static void addToGame(JoinRequest request, String username) {
        GameData requestedGame = Database.getGames().get(request.gameID());
        switch (request.playerColor()) {
            case "WHITE" -> requestedGame.whiteUsername = username;
            case "BLACK" -> requestedGame.blackUsername = username;
        }
    }

    public static void deleteAuth(String authToken) {Database.getCurrentAuth().remove(authToken);}

    public static void clearDatabase() {Database.clear();}
}
