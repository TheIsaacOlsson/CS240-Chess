package server.Handlers;

import com.google.gson.Gson;
import dataaccess.ConnectionException;
import io.javalin.http.Context;
import serverFacade.RequestResponse.CreateGameRequest;
import serverFacade.RequestResponse.CreateGameResponse;
import serverFacade.RequestResponse.ErrorResponse;
import server.Service.CreateGameService;
import server.Service.ValidateService;

import java.util.Objects;

public class CreateGameHandler {
    public static void tryCreateGame(Context ctx) {
        String authToken = ctx.header("authorization");
        var gameReq = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
        try {
            if ( ! ValidateService.isAuthorized(authToken)) {
                ctx.contentType("application/json");
                ctx.status(401);
                ctx.result(new Gson().toJson(new ErrorResponse("Error: unauthorized")));
            } else if (Objects.equals(gameReq.gameName(), "") || gameReq.gameName() == null) {
                ctx.contentType("application/json");
                ctx.status(400);
                ctx.result(new Gson().toJson(new ErrorResponse("Error: bad request")));
            } else {
                Integer newGameID = CreateGameService.makeGame(gameReq.gameName());
                ctx.contentType("application/json");
                ctx.status(200);
                ctx.result(new Gson().toJson(new CreateGameResponse(newGameID)));
            }
        } catch (ConnectionException e) {
            ctx.contentType("application/json");
            ctx.status(500);
            ctx.result(new Gson().toJson(new ErrorResponse("Error: Cannot connect to database")));
        }
    }
}
