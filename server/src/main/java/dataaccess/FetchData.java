package dataaccess;

import java.util.Map;

public interface FetchData {
    static UserData getUser(String username) {return null;}
    static AuthData getAuthData(String authToken) {return null;}
    static Map<Integer, GameData> getGames() {return null;}
}
