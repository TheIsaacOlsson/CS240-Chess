package server.Services;


import dataaccess.AuthData;
import dataaccess.MemoryWriteData;
import dataaccess.UserData;

public class RegisterService {
    public static AuthData register(UserData registration) {
        if(ValidateService.isUser(registration.username())) {
            return null;
        }
        MemoryWriteData.addUser(registration);
        return AuthService.makeAuthData(registration.username());
    }
}
