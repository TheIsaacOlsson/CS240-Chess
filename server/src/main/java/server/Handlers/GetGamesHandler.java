package server.Handlers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import server.RequestResponse.GetGamesResponse;
import server.Services.GetGamesService;
import server.Services.ValidateService;

public class GetGamesHandler {
    public static void getGames(Context ctx) {
        if (ValidateService.isAuthorized(ctx.header("authorization"))) {
            server.RequestResponse.AbbrGameData[] allGames = GetGamesService.representGames();
            ctx.contentType("application/json");
            ctx.status(200);
            ctx.result(new Gson().toJson(new GetGamesResponse(allGames, null)));
        } else {
            ctx.contentType("application/json");
            ctx.status(401);
            ctx.result(new Gson().toJson(new GetGamesResponse(null, "Error: unauthorized")));
        }
    }
}
