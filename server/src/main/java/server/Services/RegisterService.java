package server.Services;


import dataaccess.AuthData;
import dataaccess.MemoryFetchData;
import dataaccess.MemoryWriteData;
import dataaccess.UserData;

public class RegisterService {
    public static AuthData register(UserData registration) {
        if(ValidateService.exists(registration.username())) {
            return null;
        }
        MemoryWriteData.addUser(registration);
        AuthData newAuth = AuthService.makeAuthData(registration.username());
        MemoryWriteData.addAuth(newAuth);
        return newAuth;
    }
}
