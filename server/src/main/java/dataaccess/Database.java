package dataaccess;

/**
 * Switch between a Memory-based database
 * and an SQL database by changing the "type" variable
 */
public class Database {
    private enum type {
        MEMORY,
        SQL
    }
    private final static type databaseType = type.SQL;

    public static FetchData FetchData;
    public static WriteData WriteData;

    public Database() throws DataAccessException {
        switch (databaseType) {
            case MEMORY -> {
                WriteData = new MemoryWriteData();
                FetchData = new MemoryFetchData();
            }
            case SQL -> {
                WriteData = new SQLWriteData();
                FetchData = new SQLFetchData();
            }
        }
    }
}
