package dataaccess;

/**
 * This class is where you can manually switch between a Memory-based database
 * or an SQL server based database.
 */
public class Database {
    public static FetchData FetchData = new MemoryFetchData();
    public static WriteData WriteData = new MemoryWriteData();
}
