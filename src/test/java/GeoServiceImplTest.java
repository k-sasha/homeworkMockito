import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoServiceImpl;

import java.util.stream.Stream;

public class GeoServiceImplTest {
    private static long suiteStartTime;
    private long testStartTime;

    @BeforeAll
    public static void initSuite() {
        System.out.println("Tests started");
        suiteStartTime = System.nanoTime();
    }

    @AfterAll
    public static void completeSuite() {
        System.out.println("Tests completed: " + (System.nanoTime() - suiteStartTime));
    }

    @BeforeEach
    public void initTest() {
        System.out.println("Test started");
        testStartTime = System.nanoTime();
    }

    @AfterEach
    public void completeTest() {
        System.out.println("Test completed: " +
                (System.nanoTime() - testStartTime));
    }

    @ParameterizedTest
    @MethodSource("source")
    public void testByIp(String ip, String city, Country country, String street, int builing) {
        GeoServiceImpl geoService = new GeoServiceImpl();
        Location result = geoService.byIp(ip);
        Assertions.assertEquals(city, result.getCity());
        Assertions.assertEquals(country, result.getCountry());
        Assertions.assertEquals(street, result.getStreet());
        Assertions.assertEquals(builing, result.getBuiling());
    }

    private static Stream<Arguments> source() {
        return Stream.of(Arguments.of("127.0.0.1", null, null, null, 0),
                Arguments.of("172.0.32.11", "Moscow", Country.RUSSIA, "Lenina", 15),
                Arguments.of("96.44.183.149", "New York", Country.USA, " 10th Avenue", 32),
                Arguments.of("172.", "Moscow", Country.RUSSIA, null, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("source2")
    public void testByIpStartWithNumbers(String ip, String startWithNumbers, String city,
                                         Country country, String street, int builing) {
        GeoServiceImpl geoService = new GeoServiceImpl();
        Location result = geoService.byIp(ip);
        Assertions.assertTrue(ip.startsWith(startWithNumbers));
        Assertions.assertEquals(city, result.getCity());
        Assertions.assertEquals(country, result.getCountry());
        Assertions.assertEquals(street, result.getStreet());
        Assertions.assertEquals(builing, result.getBuiling());
    }

    private static Stream<Arguments> source2() {
        return Stream.of(
                Arguments.of("172.0.32.22", "172.", "Moscow", Country.RUSSIA, null, 0),
                Arguments.of("96.44.183.150", "96.", "New York", Country.USA, null, 0)
        );
    }


}
