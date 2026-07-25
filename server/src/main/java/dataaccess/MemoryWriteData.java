package dataaccess;

public class MemoryWriteData implements WriteData {
    public static void addUser(UserData newUser) {
        Database.addUser(newUser);
    }

    public static void addAuth(AuthData newAuth) {
        Database.addAuth(newAuth);
    }

    public static void addGame(GameData newGame) {Database.addGame(newGame);}

    public static void deleteAuth(String authToken) {Database.getCurrentAuth().remove(authToken);}

    public static void clearDatabase() {Database.clear();}
}
