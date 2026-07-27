package server.Service;

import dataaccess.AuthData;
import dataaccess.MemoryFetchData;
import dataaccess.UserData;

public class ValidateService {
    public static boolean isUser(String username) {
        UserData savedUser = MemoryFetchData.getUser(username);
        return savedUser != null;
    }

    public static boolean isAuthorized(String authToken) {
        AuthData auth = MemoryFetchData.getAuthData(authToken);
        return auth != null;
    }
}
