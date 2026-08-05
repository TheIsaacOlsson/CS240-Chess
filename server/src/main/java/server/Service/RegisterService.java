package server.Service;


import dataaccess.AuthData;
import dataaccess.ConnectionException;
import dataaccess.Database;
import dataaccess.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterService {
    public static AuthData register(UserData registration) throws ConnectionException {
        if(ValidateService.isUser(registration.username())) {
            return null;
        }
        String hashedPassword = BCrypt.hashpw(registration.password(), BCrypt.gensalt());
        UserData encryptedUserData = new UserData(registration.username(), hashedPassword, registration.email());
        Database.DataAccess.addUser(encryptedUserData);
        return AuthService.makeAuthData(registration.username());
    }
}
