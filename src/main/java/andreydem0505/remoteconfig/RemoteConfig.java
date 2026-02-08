package andreydem0505.remoteconfig;

import okhttp3.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class RemoteConfig {
    public static final String BASE_URL = "http://localhost:8080";
    public static final String PROPERTIES_URL = BASE_URL + "/properties";
    public static final String AUTH_URL = BASE_URL + "/auth";

    private final OkHttpClient client;
    private String jwtToken;

    public RemoteConfig(String username, String password) {
        ResourceBundle rb = ResourceBundle.getBundle("settings");

        int cacheMegabytes = Integer.parseInt(rb.getString("cache.size"));
        String cacheDirName = rb.getString("cache.dir");

        long cacheSize = cacheMegabytes * 1024 * 1024L;
        File cacheDir = new File(cacheDirName, "http_cache");
        Cache cache = new Cache(cacheDir, cacheSize);

        int maxConnections = Integer.parseInt(rb.getString("connections.max"));
        int keepAliveMs = Integer.parseInt(rb.getString("connections.keep-alive-ms"));
        ConnectionPool pool = new ConnectionPool(maxConnections, keepAliveMs, TimeUnit.MILLISECONDS);

        login(username, password);

        Interceptor preInterceptor = this::setHeaders;

        Interceptor postInterceptor = chain -> {
            Response response = chain.proceed(chain.request());

            if (response.code() == 403) {
                response.close();
                login(username, password);

                return setHeaders(chain);
            }

            return response;
        };

        client = new OkHttpClient.Builder()
                .addInterceptor(preInterceptor)
                .addInterceptor(postInterceptor)
                .retryOnConnectionFailure(true)
                .connectionPool(pool)
                .cache(cache)
                .build();
    }

    public CustomProperty createCustomProperty(String name, Object data) {
        return new CustomProperty(client, name, data);
    }

    public BooleanFeatureFlag createBooleanFeatureFlag(String name, boolean data) {
        return new BooleanFeatureFlag(client, name, data);
    }

    public PercentageFeatureFlag createPercentageFeatureFlag(String name, int data) {
        return new PercentageFeatureFlag(client, name, data);
    }

    public EqualityFeatureFlag createEqualityFeatureFlag(String name, Object data) {
        return new EqualityFeatureFlag(client, name, data);
    }

    public UnitInListFeatureFlag createUnitInListFeatureFlag(String name, Object data) {
        return new UnitInListFeatureFlag(client, name, data);
    }

    private Response setHeaders(Interceptor.Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .build();
        return chain.proceed(request);
    }

    private void login(String username, String password) {
        RequestBody body = JsonSerializer.toJson(Map.of(
                "username", username,
                "password", password
        ));
        Request request = new Request.Builder()
                .url(AUTH_URL + "/login")
                .post(body)
                .build();
        OkHttpClient localClient = new OkHttpClient.Builder().build();
        try (Response response = localClient.newCall(request).execute()) {
            new ResponseValidator(response)
                    .validateStatus(new int[] {200});
            jwtToken = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
