package dataaccess;


public class MemoryFetchData implements FetchData {
    public static UserData getUser(String username) {
        return Database.getUsers().get(username);
    }
    public static AuthData getAuthData(String authToken) {return Database.getCurrentAuth().get(authToken);}
    public static GameData[] getGames() {return Database.getGames().values().toArray(new GameData[0]);}
}
