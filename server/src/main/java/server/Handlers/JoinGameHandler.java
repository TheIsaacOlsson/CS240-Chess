package server.Handlers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import server.RequestResponse.JoinRequest;
import server.RequestResponse.JoinResponse;
import server.Services.ColorTakenException;
import server.Services.JoinGameService;
import server.Services.ValidateService;

import java.awt.*;

public class JoinGameHandler {
    public static void tryJoin(Context ctx) {
        String authToken = ctx.header("authorization");
        if ( ! ValidateService.isAuthorized(authToken)) {
            ctx.contentType("application/json");
            ctx.status(401);
            ctx.result(new Gson().toJson(new JoinResponse("Error: unauthorized")));
        } else {
            var joinReq = new Gson().fromJson(ctx.body(), JoinRequest.class);
            if (joinReq.gameID() == null || joinReq.playerColor() == null || joinReq.gameID() <= 0 || joinReq.gameID() > 9999 || ( ! joinReq.playerColor().equals("BLACK") && ! joinReq.playerColor().equals("WHITE"))) {
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
    }
}
