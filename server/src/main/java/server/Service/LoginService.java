package server.Service;

import dataaccess.AuthData;
import dataaccess.ConnectionException;
import dataaccess.Database;
import org.mindrot.jbcrypt.BCrypt;
import server.RequestResponse.LoginRequest;

public class LoginService {
    public static AuthData login(LoginRequest login) throws ConnectionException {
        if (
                ! (Database.DataAccess.getUser(login.username()) == null) &&
                        BCrypt.checkpw(login.password(), Database.DataAccess.getUser(login.username()).password())
        ) {
            return AuthService.makeAuthData(login.username());
        } else {
            return null;
        }
    }
}
