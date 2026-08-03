package dataaccess;


import java.util.Map;

public class MemoryFetchData implements FetchData {
    public static UserData getUser(String username) {
        return MemoryDatabase.getUsers().get(username);
    }
    public static AuthData getAuthData(String authToken) {return MemoryDatabase.getCurrentAuth().get(authToken);}
    public static Map<Integer, GameData> getGames() {return MemoryDatabase.getGames();}
}
