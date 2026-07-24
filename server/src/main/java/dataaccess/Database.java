package dataaccess;

import chess.ChessGame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Database {
    private static Map<String, UserData> users = new HashMap<>();
    private static Map<String, AuthData> currentAuth = new HashMap<>();
    private static Map<Integer, GameData> games = new HashMap<>();

    public Database() {}

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
