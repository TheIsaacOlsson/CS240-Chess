package server.Handlers;

import io.javalin.http.Context;
import server.Service.ClearDatabaseService;

public class ClearDataHandler {
    public static void clearData (Context ctx) {
        ClearDatabaseService.clearDatabase();
        ctx.status(200);
        // Take care of any exceptions
        // Add error message to context
    }
}
