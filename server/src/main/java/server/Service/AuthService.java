package server.Service;

import dataaccess.AuthData;
import dataaccess.Database;

import java.util.UUID;

public class AuthService {
    public static AuthData makeAuthData(String username) {
        String token = generateAuthToken();
        AuthData newAuth = new AuthData(token, username);
        Database.DataAccess.addAuth(newAuth);
        return newAuth;
    }

    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
