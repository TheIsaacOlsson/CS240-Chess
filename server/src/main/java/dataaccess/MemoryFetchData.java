package dataaccess;

import java.util.Map;

public class MemoryFetchData implements FetchData {
    public static UserData getUser(String username) {
        return Database.getUsers().get(username);
    }
}
