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

    public static AccessData DataAccess;

    public Database() throws DataAccessException {
        switch (databaseType) {
            case MEMORY -> DataAccess = new MemoryAccessData();
            case SQL -> DataAccess = new SQLAccessData();
        }
    }
}
