package server.Services;

import dataaccess.DataAccessException;
import dataaccess.MemoryWriteData;

public class LogoutService {
    public static void logout(String authToken) throws DataAccessException {
        if ( ! ValidateService.isAuthorized(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        } else {
            MemoryWriteData.deleteAuth(authToken);
        }
    }
}
