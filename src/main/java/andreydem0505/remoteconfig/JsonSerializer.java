package andreydem0505.remoteconfig;

import com.google.gson.Gson;
import okhttp3.RequestBody;

import java.util.Map;

public class JsonSerializer {
    private static final Gson gson = new Gson();

    public static RequestBody toJson(Map<String, Object> map) {
        return RequestBody.create(
                gson.toJson(map),
                okhttp3.MediaType.parse("application/json; charset=utf-8")
        );
    }
}
