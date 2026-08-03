package dataaccess;

import java.util.Map;

public interface FetchData {
    UserData getUser(String username);
    AuthData getAuthData(String authToken);
    Map<Integer, GameData> getGames();
}
