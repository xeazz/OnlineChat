import Server.Server;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestServer {
    Server sut;

    @BeforeAll
    public static void iniSut() {
        System.out.println("Running test");
    }

    @AfterAll
    public static void completeSut() {
        System.out.println("Test completed");
    }

    @BeforeEach
    public void initEachTestFourth() {

        sut = new Server();
        System.out.println(Server.class + " создан");
    }

    @AfterEach
    public void afterEachTestFourth() {
        sut = null;
        System.out.println(Server.class + " обнулен");
    }

    @Test
    public void checkNameTest() {
        String name = "Ivan";
        Boolean expected = true;
        Boolean result = sut.checkName(name);
        assertEquals(expected, result);
    }
}
