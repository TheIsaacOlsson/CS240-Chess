package server.Services;

import dataaccess.MemoryFetchData;
import dataaccess.UserData;

public class ValidateService {
    public static boolean exists(String username) {
        UserData savedUser = new MemoryFetchData().getUser(username);
        return savedUser != null;
    }


}
