package dataaccess;

import chess.ChessGame;

import java.util.HashSet;
import java.util.Set;

public class Database {
    private static Set<UserData> users = new HashSet<>();
    private static Set<AuthData> currentAuth = new HashSet<>();
    private static Set<GameData> games = new HashSet<>();

    public Database() {}

    public static Set<UserData> getUsers() {
        return users;
    }

    public static Set<AuthData> getCurrentAuth() {
        return currentAuth;
    }

    public static Set<GameData> getGames() {
        return games;
    }

    public static void clear() {
        users.clear();
        currentAuth.clear();
        games.clear();
    }

    public static void addUser(UserData user) {
        users.add(user);
    }

    public static void addAuth(AuthData auth) {
        currentAuth.add(auth);
    }

    public static void addGame(GameData game) {
        games.add(game);
    }
}
