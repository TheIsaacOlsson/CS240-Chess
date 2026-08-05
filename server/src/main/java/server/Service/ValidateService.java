package server.Service;

import dataaccess.AuthData;
import dataaccess.ConnectionException;
import dataaccess.Database;
import dataaccess.UserData;

public class ValidateService {
    public static boolean isUser(String username) throws ConnectionException {
        UserData savedUser = Database.DataAccess.getUser(username);
        return savedUser != null;
    }

    public static boolean isAuthorized(String authToken) throws ConnectionException {
        AuthData auth = Database.DataAccess.getAuthData(authToken);
        return auth != null;
    }
}
