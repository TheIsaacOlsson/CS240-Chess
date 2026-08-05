package server.Service;

import dataaccess.ConnectionException;
import dataaccess.Database;

public class ClearDatabaseService {
    public static void clearDatabase() throws ConnectionException {
        Database.DataAccess.clearDatabase();
    }
}
