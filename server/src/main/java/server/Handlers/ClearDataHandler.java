package server.Handlers;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import server.Services.ClearDatabaseService;

public class ClearDataHandler {
    public void clearData (Context ctx) {
        ClearDatabaseService.clearDatabase();
        ctx.status(200);
        // Take care of any exceptions
        // Add error message to context
    }
}
