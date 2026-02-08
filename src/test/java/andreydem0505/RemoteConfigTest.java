package andreydem0505;

import andreydem0505.remoteconfig.*;
import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RemoteConfigTest extends TestData {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "qwerty123";

    private static RemoteConfig rc;

    @BeforeAll
    public static void setUp() throws IOException {
        registerUser();
        rc = new RemoteConfig(USERNAME, PASSWORD);
    }

    private static void registerUser() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        RequestBody body = JsonSerializer.toJson(Map.of(
                "username", USERNAME,
                "password", PASSWORD
        ));
        Request request = new Request.Builder()
                .url(RemoteConfig.AUTH_URL + "/register")
                .post(body)
                .build();
        client.newCall(request).execute();
    }

    @Test
    public void testCustomProperty() {
        CustomProperty settings = rc.createCustomProperty("settings", customPropertyDataMap);
        Assertions.assertEquals(customPropertyExpectedJson, settings.getData());
    }

    @ParameterizedTest
    @CsvSource({"isDarkTheme,true", "notifications.enabled,false"})
    public void testBooleanFeatureFlag(String name, boolean expected) {
        boolean actual = rc.createBooleanFeatureFlag(name, expected).isEnabled();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testPercentageFeatureFlag() {
        boolean value = rc.createPercentageFeatureFlag("traffic.percentage", 100).isEnabled();
        Assertions.assertTrue(value);
    }

    @Test
    public void testStringEqualityFeatureFlag() {
        EqualityFeatureFlag flag = rc.createEqualityFeatureFlag("language", "English");
        Assertions.assertTrue(flag.isEnabled("English"));
        Assertions.assertFalse(flag.isEnabled("Russian"));
    }

    @Test
    public void testMapEqualityFeatureFlag() {
        Map<String, Object> body = Map.of(
                "theme", "dark",
                "notifications", Map.of("enabled", false)
        );
        EqualityFeatureFlag flag = rc.createEqualityFeatureFlag("user.settings", body);
        Assertions.assertTrue(flag.isEnabled(body));
        Assertions.assertFalse(flag.isEnabled(Map.of(
                "theme", "dark",
                "notifications", Map.of("enabled", true)
        )));
    }

    @Test
    public void testArrayEqualityFeatureFlag() {
        String[] body = new String[] {"English", "Russian"};
        EqualityFeatureFlag flag = rc.createEqualityFeatureFlag("languages", body);
        Assertions.assertTrue(flag.isEnabled(body));
        Assertions.assertFalse(flag.isEnabled(new String[] {"English", "German"}));
    }

    @Test
    public void testListEqualityFeatureFlag() {
        List<String> body = List.of("Mike", "Andrew", "Sara");
        EqualityFeatureFlag flag = rc.createEqualityFeatureFlag("admins", body);
        Assertions.assertTrue(flag.isEnabled(body));
        Assertions.assertFalse(flag.isEnabled(List.of("John", "Andrew", "Sara")));
    }

    @Test
    public void testNullEqualityFeatureFlag() {
        EqualityFeatureFlag flag = rc.createEqualityFeatureFlag("proxy", null);
        Assertions.assertTrue(flag.isEnabled(null));
        Assertions.assertFalse(flag.isEnabled("192.168.1.1"));
    }

    @ParameterizedTest
    @CsvSource({"iOS,true", "MacOS,false"})
    public void testUnitInListFeatureFlag(Object payload, boolean expected) {
        String[] body = new String[] {"Android", "iOS", "Windows"};
        UnitInListFeatureFlag flag = rc.createUnitInListFeatureFlag("os", body);
        Assertions.assertEquals(expected, flag.isEnabled(payload));
    }
}
