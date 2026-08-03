package dataaccess;

import java.util.HashMap;
import java.util.Map;

public class MemoryDatabase {
    private static Map<String, UserData> users = new HashMap<>();
    private static Map<String, AuthData> currentAuth = new HashMap<>();
    private static Map<Integer, GameData> games = new HashMap<>();

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

    public static void addGame(GameData game) {
        games.put(game.getGameID(), game);
    }
}
