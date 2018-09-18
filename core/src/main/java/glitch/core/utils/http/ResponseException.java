package glitch.core.utils.http;

import java.io.IOException;

public class ResponseException extends IOException {
    private final ResponseError error;

    public ResponseException(ResponseError error) {
        super(String.format("%s [%d]: %s", error.getError(), error.getStatus(), error.getMessage()));
        this.error = error;
    }

    public ResponseError getError() {
        return error;
    }
}
