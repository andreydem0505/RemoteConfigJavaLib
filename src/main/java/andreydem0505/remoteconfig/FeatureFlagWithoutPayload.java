package andreydem0505.remoteconfig;

import okhttp3.OkHttpClient;

abstract class FeatureFlagWithoutPayload extends FeatureFlag {
    protected FeatureFlagWithoutPayload(OkHttpClient client, String name, PropertyType type, Object data) {
        super(client, name, type, data);
    }

    public Boolean isEnabled() {
        return retrieveValue();
    }
}
