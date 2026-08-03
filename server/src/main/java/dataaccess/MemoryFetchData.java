package dataaccess;


import java.util.Map;

public class MemoryFetchData implements FetchData {
    public UserData getUser(String username) {
        return MemoryDatabase.getUsers().get(username);
    }
    public AuthData getAuthData(String authToken) {return MemoryDatabase.getCurrentAuth().get(authToken);}
    public Map<Integer, GameData> getGames() {return MemoryDatabase.getGames();}
}
