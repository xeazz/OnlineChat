import Server.Server;
import Settings.LoadSettings;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestLoadSettings {
    LoadSettings sut;

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

        sut = new LoadSettings();
        System.out.println(LoadSettings.class + " создан");
    }

    @AfterEach
    public void afterEachTestFourth() {
        sut = null;
        System.out.println(LoadSettings.class + " обнулен");
    }

    @Test
    public void getAddressTestFirst() {
        String expected = "127.0.0.1";
        String result = sut.getAddress();
        assertEquals(expected, result);
    }

    @Test
    public void getAddressTestSecond() {
        String expected = "localhost";
        String result = sut.getAddress();
        assertEquals(expected, result);
    }

    @Test
    public void getPortTestFist() {
        int expected = 3387;
        int result = sut.getPort();
        assertEquals(expected, result);
    }

    @Test
    public void getPortTestSecond() {
        int expected = 7899;
        int result = sut.getPort();
        assertEquals(expected, result);
    }
}