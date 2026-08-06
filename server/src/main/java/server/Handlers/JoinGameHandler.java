package server.Handlers;

import com.google.gson.Gson;
import dataaccess.ConnectionException;
import io.javalin.http.Context;
import serverFacade.ErrorResponse;
import server.RequestResponse.JoinRequest;
import server.RequestResponse.JoinResponse;
import server.Service.ColorTakenException;
import server.Service.JoinGameService;
import server.Service.ValidateService;

public class JoinGameHandler {
    public static void tryJoin(Context ctx) {
        String authToken = ctx.header("authorization");
        try {
            if (!ValidateService.isAuthorized(authToken)) {
                ctx.contentType("application/json");
                ctx.status(401);
                ctx.result(new Gson().toJson(new JoinResponse("Error: unauthorized")));
            } else {
                var joinReq = new Gson().fromJson(ctx.body(), JoinRequest.class);
                if (joinReq.gameID() == null || joinReq.playerColor() == null
                        || joinReq.gameID() <= 0 || joinReq.gameID() > 9999) {
                    ctx.contentType("application/json");
                    ctx.status(400);
                    ctx.result(new Gson().toJson(new JoinResponse("Error: bad request")));
                } else {
                    try {
                        JoinGameService.joinGame(joinReq, authToken);
                        ctx.contentType("application/json");
                        ctx.status(200);
                        ctx.result(new Gson().toJson(new JoinResponse(null)));
                    } catch (ColorTakenException e) {
                        ctx.contentType("application/json");
                        ctx.status(403);
                        ctx.result(new Gson().toJson(new JoinResponse(e.getMessage())));
                    }
                }
            }
        } catch (ConnectionException e) {
            ctx.contentType("application/json");
            ctx.status(500);
            ctx.result(new Gson().toJson(new ErrorResponse("Error: Cannot connect to database")));
        }
    }
}
