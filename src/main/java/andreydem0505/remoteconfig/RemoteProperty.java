package andreydem0505.remoteconfig;

import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


abstract class RemoteProperty {
    protected final OkHttpClient client;
    protected Request dataRetrievalRequest;

    protected RemoteProperty(OkHttpClient client, String name, PropertyType type, Object data) {
        this.client = client;
        sendCreationRequest(name, type, data);
        initDataRetrievalRequest(name);
    }

    protected void initDataRetrievalRequest(String name) {
        this.dataRetrievalRequest = new Request.Builder()
                .url(RemoteConfig.PROPERTIES_URL + "/" + name)
                .get()
                .build();
    }

    private void sendCreationRequest(String name, PropertyType type, Object data) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", name);
        payload.put("type", type.name());
        payload.put("data", data);
        Request request = new Request.Builder()
                .url(RemoteConfig.PROPERTIES_URL)
                .post(JsonSerializer.toJson(payload))
                .build();
        try (Response response = client.newCall(request).execute()) {
            new ResponseValidator(response)
                    .validateStatus(new int[] {201, 409});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
