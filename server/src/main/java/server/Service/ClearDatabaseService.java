package server.Service;

import dataaccess.Database;

public class ClearDatabaseService {
    public static void clearDatabase(){
        Database.DataAccess.clearDatabase();
    }
}
