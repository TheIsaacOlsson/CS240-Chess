package dataaccess;

import jdk.jshell.spi.ExecutionControl;

import java.util.Map;

public class SQLFetchData implements FetchData {
    public UserData getUser(String username) {
        return null;
    }
    public AuthData getAuthData(String authToken) {return null;}
    public Map<Integer, GameData> getGames() {return null;}
}
