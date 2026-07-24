package server.Services;

import dataaccess.AuthData;
import dataaccess.MemoryWriteData;

import java.util.UUID;

public class AuthService {
    public static AuthData makeAuthData(String username) {
        String token = generateAuthToken();
        AuthData newAuth = new AuthData(token, username);
        MemoryWriteData.addAuth(newAuth);
        return newAuth;
    }

    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
