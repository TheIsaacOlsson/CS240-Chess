package server.Services;


import dataaccess.MemoryFetchData;
import server.RequestResponse.RegisterRequest;

public class RegisterService {
    public void register(RegisterRequest registration) {
        new MemoryFetchData().getUser(registration.username());
    }
}
