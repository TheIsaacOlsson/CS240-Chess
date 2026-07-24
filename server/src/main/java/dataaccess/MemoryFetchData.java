package dataaccess;

public class MemoryFetchData implements FetchData {
    public UserData getUser(String username) {
        return Database.getUsers().get(username);
    }
}
