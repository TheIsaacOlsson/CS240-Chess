package server.Handlers;

import com.google.gson.Gson;
import dataaccess.AuthData;
import dataaccess.ConnectionException;
import dataaccess.UserData;
import io.javalin.http.Context;
import serverFacade.ErrorResponse;
import server.RequestResponse.RegisterResponse;
import server.Service.RegisterService;

public class RegisterHandler {
    public static void tryRegister(Context ctx) {
        try {
            var registerReq = new Gson().fromJson(ctx.body(), UserData.class);
            if (registerReq.username() == null || registerReq.password() == null || registerReq.email() == null) {
                ctx.contentType("application/json");
                ctx.status(400);
                ctx.result(new Gson().toJson(new ErrorResponse("Error: Empty field")));
            } else {
                AuthData newAuth = RegisterService.register(registerReq);
                if (newAuth == null) {
                    ctx.contentType("application/json");
                    ctx.status(403);
                    ctx.result(new Gson().toJson(new ErrorResponse("Error: Username already taken")));
                } else {
                    RegisterResponse response = new RegisterResponse(registerReq.username(), newAuth.authToken());
                    ctx.contentType("application/json");
                    ctx.result(new Gson().toJson(response));
                }
            }
        } catch (ConnectionException e) {
            ctx.contentType("application/json");
            ctx.status(500);
            ctx.result(new Gson().toJson(new ErrorResponse("Error: Cannot connect to database")));
        }
    }
}
