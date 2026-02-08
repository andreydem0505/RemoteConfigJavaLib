package andreydem0505.remoteconfig.exception;

import java.util.Arrays;

public class UnexpectedStatusCodeException extends RuntimeException {
    public UnexpectedStatusCodeException(int[] expected, int actual) {
        super("Got " + actual + " status code, but " + Arrays.toString(expected) + " were expected");
    }
}
