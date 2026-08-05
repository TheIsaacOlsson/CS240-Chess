package server.Service;


import dataaccess.AuthData;
import dataaccess.ConnectionException;
import dataaccess.Database;
import dataaccess.UserData;

public class RegisterService {
    public static AuthData register(UserData registration) throws ConnectionException {
        if(ValidateService.isUser(registration.username())) {
            return null;
        }
        Database.DataAccess.addUser(registration);
        return AuthService.makeAuthData(registration.username());
    }
}
