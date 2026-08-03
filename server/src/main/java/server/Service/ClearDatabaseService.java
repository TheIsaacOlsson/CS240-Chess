package server.Service;

import dataaccess.Database;
import dataaccess.MemoryWriteData;

public class ClearDatabaseService {
    public static void clearDatabase(){
        Database.WriteData.clearDatabase();
    }
}
