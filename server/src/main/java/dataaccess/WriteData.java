package dataaccess;

public interface WriteData {
    static void addUser(UserData newUser) {}
    static void addAuth(AuthData newAuth) {}
    static void addGame(GameData newGame) {}
    static void deleteAuth(AuthData auth) {}
    static void clearDatabase() {}
}
