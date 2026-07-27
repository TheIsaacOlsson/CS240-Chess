package server.Handlers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import server.RequestResponse.CreateGameRequest;
import server.RequestResponse.CreateGameResponse;
import server.Service.CreateGameService;
import server.Service.ValidateService;

import java.util.Objects;

public class CreateGameHandler {
    public static void tryCreateGame(Context ctx) {
        String authToken = ctx.header("authorization");
        var gameReq = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
        if ( ! ValidateService.isAuthorized(authToken)) {
            ctx.contentType("application/json");
            ctx.status(401);
            ctx.result(new Gson().toJson(new CreateGameResponse(null, "Error: unauthorized")));
        } else if (Objects.equals(gameReq.gameName(), "") || gameReq.gameName() == null) {
            ctx.contentType("application/json");
            ctx.status(400);
            ctx.result(new Gson().toJson(new CreateGameResponse(null, "Error: bad request")));
        } else {
            Integer newGameID = CreateGameService.makeGame(gameReq.gameName());
            ctx.contentType("application/json");
            ctx.status(200);
            ctx.result(new Gson().toJson(new CreateGameResponse(newGameID, null)));
        }
    }
}
