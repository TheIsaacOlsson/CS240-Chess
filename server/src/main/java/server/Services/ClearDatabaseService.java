package server.Services;

import dataaccess.DataAccessException;
import dataaccess.MemoryWriteData;

public class ClearDatabaseService {
    public static void clearDatabase(){
        MemoryWriteData.clearDatabase();
    }
}
