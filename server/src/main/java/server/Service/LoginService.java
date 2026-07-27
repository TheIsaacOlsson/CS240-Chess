package server.Service;

import dataaccess.AuthData;
import dataaccess.MemoryFetchData;
import server.RequestResponse.LoginRequest;

public class LoginService {
    public static AuthData login(LoginRequest login) {
        if (
                ! (MemoryFetchData.getUser(login.username()) == null) &&
                login.password().equals(MemoryFetchData.getUser(login.username()).password())
        ) {
            return AuthService.makeAuthData(login.username());
        } else {
            return null;
        }
    }
}
