package andreydem0505.remoteconfig;

import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.io.IOException;

public class CustomProperty extends RemoteProperty {

    protected CustomProperty(OkHttpClient client, String name, Object data) {
        super(client, name, PropertyType.CUSTOM_PROPERTY, data);
    }

    public String getData() {
        try (Response response = client.newCall(dataRetrievalRequest).execute()) {
            new ResponseValidator(response)
                    .validateStatus(new int[] {200});
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
