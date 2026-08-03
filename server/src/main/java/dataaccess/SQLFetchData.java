package dataaccess;

import jdk.jshell.spi.ExecutionControl;

import java.util.Map;

public class SQLFetchData implements FetchData {
    public static UserData getUser(String username) {
        return null;
    }
    public static AuthData getAuthData(String authToken) {return null;}
    public static Map<Integer, GameData> getGames() {return null;}
}
