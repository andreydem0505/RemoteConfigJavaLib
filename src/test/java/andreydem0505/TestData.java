package andreydem0505;

import java.util.Map;

public abstract class TestData {
    protected static final Map<String, Object> customPropertyDataMap = Map.of(
            "color.scheme", "dark",
            "items.per.page", 20,
            "show.notifications", true,
            "inner.map", Map.of(
                    "level1", Map.of(
                            "array", new int[]{1, 2, 3}
                    )
            ));
    protected static final String customPropertyExpectedJson = "{\"items.per.page\":20,\"show.notifications\":true,\"color.scheme\":\"dark\",\"inner.map\":{\"level1\":{\"array\":[1,2,3]}}}";
}
