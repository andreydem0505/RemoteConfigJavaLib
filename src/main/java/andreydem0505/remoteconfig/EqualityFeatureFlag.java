package andreydem0505.remoteconfig;

import okhttp3.OkHttpClient;

public class EqualityFeatureFlag extends FeatureFlagWithPayload {
    protected EqualityFeatureFlag(OkHttpClient client, String name, Object data) {
        super(client, name, PropertyType.EQUALITY_FEATURE_FLAG, data);
    }
}
