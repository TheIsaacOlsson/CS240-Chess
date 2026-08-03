package server.Service;

import dataaccess.AuthData;
import dataaccess.Database;
import dataaccess.MemoryFetchData;
import server.RequestResponse.LoginRequest;

public class LoginService {
    public static AuthData login(LoginRequest login) {
        if (
                ! (Database.FetchData.getUser(login.username()) == null) &&
                login.password().equals(Database.FetchData.getUser(login.username()).password())
        ) {
            return AuthService.makeAuthData(login.username());
        } else {
            return null;
        }
    }
}
