package SQLTests;

import dataaccess.AuthData;
import dataaccess.MemoryDatabase;
import dataaccess.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import server.Service.RegisterService;

public class SQLTests {

    @Test
    @Order(1)
    @DisplayName("Registration Success")
    public void registrationSuccess() {
        UserData newUser = new UserData("my_username", "my_password", "my_email@email.git");
        AuthData newUserAuth = RegisterService.register(newUser);
        Assertions.assertFalse(MemoryDatabase.getUsers().isEmpty(), "Database is empty");
        Assertions.assertTrue(MemoryDatabase.getUsers().containsKey("my_username"), "Username not found");

        Assertions.assertNotNull(newUserAuth);
        Assertions.assertFalse(MemoryDatabase.getCurrentAuth().isEmpty(), "No AuthData saved");
        Assertions.assertTrue(MemoryDatabase.getCurrentAuth().containsKey(newUserAuth.authToken()));
    }

    @Test
    @Order(2)
    @DisplayName("Username Taken")
    public void usernameTakenFailure() {
        UserData userOne = new UserData("my_username", "my_password", "my_email@email.git");
        RegisterService.register(userOne);

        UserData userTwo = new UserData("my_username", "new_pass", "different@email.yahoo");
        AuthData secondUserAuth = RegisterService.register(userTwo);

        Assertions.assertNull(secondUserAuth);
        Assertions.assertTrue(MemoryDatabase.getUsers().containsValue(userOne));
    }

}
