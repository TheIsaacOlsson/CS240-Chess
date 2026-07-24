package server.Services;

import dataaccess.AuthData;

import java.util.UUID;

public class AuthService {
    public static AuthData makeAuthData(String username) {
        String token = generateAuthToken();
        return new AuthData(token, username);
    }

    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
