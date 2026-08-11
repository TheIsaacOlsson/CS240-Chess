package dataaccess;

import serverFacade.ChessData.AuthData;
import serverFacade.ChessData.GameData;
import serverFacade.ChessData.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryDatabase {
    private static Map<String, UserData> users = new HashMap<>();
    private static Map<String, AuthData> currentAuth = new HashMap<>();
    private static Map<Integer, GameData> games = new HashMap<>();
    private static Integer nextID = 1;

    public MemoryDatabase() {}

    public static Map<String, UserData> getUsers() {
        return users;
    }

    public static Map<String, AuthData> getCurrentAuth() {
        return currentAuth;
    }

    public static Map<Integer, GameData> getGames() {
        return games;
    }

    public static void clear() {
        users.clear();
        currentAuth.clear();
        games.clear();
    }

    public static void addUser(UserData user) {
        users.put(user.username(), user);
    }

    public static void addAuth(AuthData auth) {
        currentAuth.put(auth.authToken(), auth);
    }

    public static Integer addGame(GameData game) {
        game.setGameID(generateID());
        games.put(game.getGameID(), game);
        return game.getGameID();
    }

    public static void updateGame(GameData updatedGame) {
        games.put(updatedGame.getGameID(), updatedGame);
    }

    private static Integer generateID() {
        Map<Integer, GameData> allGames = getGames();
        while (allGames.containsKey(nextID)) {
            if (nextID == 9999) {
                nextID = 1;
            } else {
                nextID++;
            }
        }
        return nextID++;
    }
}
