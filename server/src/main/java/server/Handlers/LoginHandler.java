package server.Handlers;

import com.google.gson.Gson;
import serverFacade.ChessData.AuthData;
import dataaccess.ConnectionException;
import io.javalin.http.Context;
import serverFacade.RequestResponse.ErrorResponse;
import serverFacade.RequestResponse.LoginRequest;
import serverFacade.RequestResponse.LoginResponse;
import server.Service.LoginService;

public class LoginHandler {
    public static void tryLogin(Context ctx) {
        try {
            var loginReq = new Gson().fromJson(ctx.body(), LoginRequest.class);
            if (loginReq.username() == null || loginReq.password() == null) {
                ctx.contentType("application/json");
                ctx.status(400);
                ctx.result(new Gson().toJson(new ErrorResponse("Error: Empty field")));
            } else {
                AuthData newAuth = LoginService.login(loginReq);
                if (newAuth == null) {
                    ctx.contentType("application/json");
                    ctx.status(401);
                    ctx.result(new Gson().toJson(new ErrorResponse("Error: Unauthorized")));
                } else {
                    ctx.contentType("application/json");
                    ctx.status(200);
                    ctx.result(new Gson().toJson(new LoginResponse(newAuth.username(), newAuth.authToken())));
                }
            }
        } catch (ConnectionException e) {
            ctx.contentType("application/json");
            ctx.status(500);
            ctx.result(new Gson().toJson(new ErrorResponse("Error: Cannot connect to database")));
        }
    }
}
