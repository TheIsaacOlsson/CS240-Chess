package SQLTests;

import dataaccess.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.Service.RegisterService;
import server.Service.ValidateService;

public class SQLTests {
    private static Server server;

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        try {
            new Database();
        } catch (DataAccessException e) {
            System.out.printf("Database was not able to initialize: %s", e.getMessage());
        }
        System.out.println("Started test HTTP server on " + port);
    }

    @Test
    @Order(1)
    @DisplayName("Registration Success")
    public void registrationSuccess() {
        UserData newUser = new UserData("my_username", "my_password", "my_email@email.git");
        AuthData newUserAuth = RegisterService.register(newUser);

        Assertions.assertTrue(ValidateService.isUser(newUser.username())); // Verifies user got made if not already there
        Assertions.assertNotNull(newUserAuth); // Verifies that user was not already there (Clear database first)
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
        Assertions.assertTrue(ValidateService.isUser(userOne.username()));
    }
}
