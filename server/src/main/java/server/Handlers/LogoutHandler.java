package server.Handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import server.RequestResponse.LogoutResponse;
import server.Service.LogoutService;

public class LogoutHandler {
    public static void tryLogout(Context ctx) {
        String authToken = ctx.header("authorization");
        try {
            LogoutService.logout(authToken);
            ctx.contentType("application/json");
            ctx.status(200);
            ctx.result(new Gson().toJson(new LogoutResponse(null)));
        } catch (DataAccessException err) {
            ctx.contentType("application/json");
            ctx.status(401);
            ctx.result(new Gson().toJson(new LogoutResponse(err.getMessage())));
        }
    }
}
