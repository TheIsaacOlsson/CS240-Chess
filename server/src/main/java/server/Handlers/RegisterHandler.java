package server.Handlers;

import com.google.gson.Gson;
import dataaccess.AuthData;
import dataaccess.UserData;
import io.javalin.http.Context;
import server.RequestResponse.RegisterResponse;
import server.Services.RegisterService;

public class RegisterHandler {
    public void tryRegister(Context registration) {
        var registerReq = new Gson().fromJson(registration.body(), UserData.class);
        AuthData newAuth = RegisterService.register(registerReq);
        if (newAuth == null) {
            registration.contentType("application/json");
            registration.status(403);
            registration.result(new Gson().toJson(new RegisterResponse(null, null, "Error: Username already taken")));
        } else {
            RegisterResponse response = new RegisterResponse(registerReq.username(), newAuth.authToken(), null);
            registration.contentType("application/json");
            registration.result(new Gson().toJson(response));
        }
    }
}
