package server.Handlers;

import com.google.gson.Gson;
import dataaccess.AuthData;
import io.javalin.http.Context;
import server.RequestResponse.LoginRequest;
import server.RequestResponse.LoginResponse;
import server.Services.LoginService;

public class LoginHandler {
    public static void tryLogin(Context ctx) {
        var loginReq = new Gson().fromJson(ctx.body(), LoginRequest.class);
        if(loginReq.username() == null || loginReq.password() == null) {
            ctx.contentType("application/json");
            ctx.status(400);
            ctx.result(new Gson().toJson(new LoginResponse(null, null, "Error: Empty field")));
        } else {
            AuthData newAuth = LoginService.login(loginReq);
            if (newAuth == null) {
                ctx.contentType("application/json");
                ctx.status(401);
                ctx.result(new Gson().toJson(new LoginResponse(null, null, "Error: Unauthorized")));
            } else {
                ctx.contentType("application/json");
                ctx.status(200);
                ctx.result(new Gson().toJson(new LoginResponse(newAuth.username(), newAuth.authToken(), null)));
            }
        }
    }
}
