package server.Service;

import dataaccess.MemoryWriteData;

public class ClearDatabaseService {
    public static void clearDatabase(){
        MemoryWriteData.clearDatabase();
    }
}
