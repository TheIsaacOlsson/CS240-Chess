package server.Service;

import dataaccess.AuthData;
import dataaccess.Database;
import dataaccess.MemoryFetchData;
import dataaccess.UserData;

public class ValidateService {
    public static boolean isUser(String username) {
        UserData savedUser = Database.FetchData.getUser(username);
        return savedUser != null;
    }

    public static boolean isAuthorized(String authToken) {
        AuthData auth = Database.FetchData.getAuthData(authToken);
        return auth != null;
    }
}
