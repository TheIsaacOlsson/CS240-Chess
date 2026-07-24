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
        return AuthService.makeAuthData(registration.username());
    }
}
