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
        new MemoryWriteData().addUser(registration);
        //Add and return new authData
        return new AuthData("0", registration.username());
    }
}
