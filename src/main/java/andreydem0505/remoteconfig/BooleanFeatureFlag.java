package andreydem0505.remoteconfig;

import okhttp3.OkHttpClient;

public class BooleanFeatureFlag extends FeatureFlagWithoutPayload {
    protected BooleanFeatureFlag(OkHttpClient client, String name, boolean data) {
        super(client, name, PropertyType.BOOLEAN_FEATURE_FLAG, data);
    }
}
