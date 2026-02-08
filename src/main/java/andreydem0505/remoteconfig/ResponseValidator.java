package andreydem0505.remoteconfig;

import andreydem0505.remoteconfig.exception.UnexpectedStatusCodeException;
import okhttp3.Response;

import java.util.Arrays;

class ResponseValidator {
    private Response response;

    protected ResponseValidator(Response response) {
        this.response = response;
    }

    protected ResponseValidator validateStatus(int[] statuses) {
        if (Arrays.stream(statuses).noneMatch(code -> code == response.code())) {
            throw new UnexpectedStatusCodeException(statuses, response.code());
        }
        return this;
    }
}
