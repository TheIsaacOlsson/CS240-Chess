package server.Handlers;

import com.google.gson.Gson;
import dataaccess.ConnectionException;
import io.javalin.http.Context;
import serverFacade.ErrorResponse;
import server.RequestResponse.GetGamesResponse;
import server.Service.GetGamesService;
import server.Service.ValidateService;

public class GetGamesHandler {
    public static void getGames(Context ctx) {
        try {
            if (ValidateService.isAuthorized(ctx.header("authorization"))) {
                server.RequestResponse.AbbrGameData[] allGames = GetGamesService.representGames();
                ctx.contentType("application/json");
                ctx.status(200);
                ctx.result(new Gson().toJson(new GetGamesResponse(allGames)));
            } else {
                ctx.contentType("application/json");
                ctx.status(401);
                ctx.result(new Gson().toJson(new ErrorResponse("Error: unauthorized")));
            }
        } catch (ConnectionException e) {
            ctx.contentType("application/json");
            ctx.status(500);
            ctx.result(new Gson().toJson(new ErrorResponse("Error: Cannot connect to database")));
        }
    }
}
