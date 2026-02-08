package andreydem0505.remoteconfig;

import okhttp3.OkHttpClient;

public class PercentageFeatureFlag extends FeatureFlagWithoutPayload {
    protected PercentageFeatureFlag(OkHttpClient client, String name, int data) {
        super(client, name, PropertyType.PERCENTAGE_FEATURE_FLAG, data);
    }
}
