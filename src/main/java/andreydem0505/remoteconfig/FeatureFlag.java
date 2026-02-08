package andreydem0505.remoteconfig;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

abstract class FeatureFlag extends RemoteProperty {

    protected FeatureFlag(OkHttpClient client, String name, PropertyType type, Object data) {
        if (type == PropertyType.CUSTOM_PROPERTY) {
            throw new IllegalArgumentException("FeatureFlag cannot be of type CUSTOM_PROPERTY");
        }
        super(client, name, type, data);
    }

    @Override
    protected void initDataRetrievalRequest(String name) {
        this.dataRetrievalRequest = new Request.Builder()
                .url(RemoteConfig.PROPERTIES_URL + "/check/" + name)
                .post(RequestBody.EMPTY)
                .build();
    }

    protected Boolean retrieveValue() {
        try (Response response = client.newCall(dataRetrievalRequest).execute()) {
            new ResponseValidator(response)
                    .validateStatus(new int[] {200});
            return Boolean.parseBoolean(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
