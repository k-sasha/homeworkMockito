import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

public class MessageSenderImplTest {
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

    @Test
    public void testSendRussianText() {
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp("172.0.32.16")).thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");

        MessageSenderImpl messageSenderImpl = new MessageSenderImpl(geoService, localizationService);
        String IP_ADDRESS_HEADER = "x-real-ip";
        String RUSSIAN_IP = "172.0.32.16";

        Map<String, String> headers = new HashMap<>();
        headers.put(IP_ADDRESS_HEADER, RUSSIAN_IP);
        String result = messageSenderImpl.send(headers);
        Location location = geoService.byIp(RUSSIAN_IP);
        String expected = localizationService.locale(location.getCountry());
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testSendEnglishText() {
        GeoService geoService = Mockito.mock(GeoService.class);
        Mockito.when(geoService.byIp("96.44.183.150")).thenReturn(new Location("New York", Country.USA, null, 0));

        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(Country.USA)).thenReturn("Welcome");

        MessageSenderImpl messageSenderImpl = new MessageSenderImpl(geoService, localizationService);
        String IP_ADDRESS_HEADER = "x-real-ip";
        String AMERICAN_IP = "96.44.183.150";

        Map<String, String> headers = new HashMap<>();
        headers.put(IP_ADDRESS_HEADER, AMERICAN_IP);
        String result = messageSenderImpl.send(headers);
        Location location = geoService.byIp(AMERICAN_IP);
        String expected = localizationService.locale(location.getCountry());
        Assertions.assertEquals(expected, result);
    }
}
