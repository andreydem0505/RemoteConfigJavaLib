package andreydem0505.remoteconfig;

import okhttp3.OkHttpClient;

public class UnitInListFeatureFlag extends FeatureFlagWithPayload {
    protected UnitInListFeatureFlag(OkHttpClient client, String name, Object data) {
        super(client, name, PropertyType.UNIT_IN_LIST_FEATURE_FLAG, data);
    }
}
