package server.Handlers;

import com.google.gson.Gson;
import dataaccess.ConnectionException;
import io.javalin.http.Context;
import serverFacade.RequestResponse.ErrorResponse;
import server.Service.ClearDatabaseService;

public class ClearDataHandler {
    public static void clearData (Context ctx) {
        try {
            ClearDatabaseService.clearDatabase();
            ctx.status(200);
        } catch (ConnectionException e) {
            ctx.contentType("application/json");
            ctx.status(500);
            ctx.result(new Gson().toJson(new ErrorResponse("Error: Cannot connect to database")));
        }
    }
}
