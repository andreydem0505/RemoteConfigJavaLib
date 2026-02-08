package andreydem0505.remoteconfig;

import okhttp3.OkHttpClient;

import java.util.HashMap;
import java.util.Map;

abstract class FeatureFlagWithPayload extends FeatureFlag {
    protected FeatureFlagWithPayload(OkHttpClient client, String name, PropertyType type, Object data) {
        super(client, name, type, data);
    }

    public Boolean isEnabled(Object context) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("context", context);
        dataRetrievalRequest = dataRetrievalRequest.newBuilder()
                .post(JsonSerializer.toJson(payload))
                .build();
        return retrieveValue();
    }
}
