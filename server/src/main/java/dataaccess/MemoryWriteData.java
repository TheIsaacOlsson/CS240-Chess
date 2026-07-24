package dataaccess;

public class MemoryWriteData implements WriteData {
    public void addUser(UserData newUser) {
        Database.addUser(newUser);
    }
}
