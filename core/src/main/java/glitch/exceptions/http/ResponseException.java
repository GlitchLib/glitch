package glitch.exceptions.http;

import glitch.api.http.HttpResponse;
import glitch.exceptions.GlitchException;

public class ResponseException extends GlitchException {
    private final int status;
    private final String error;
    private final String message;

    public ResponseException(int status, String error, String message) {
        super(String.format("[%s] %s", status, (message != null && !message.equals("")) ? message : error));
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public ResponseException(HttpResponse.Status status) {
        this(status.getCode(), status.getMessage(), null);
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getStatusMessage() {
        return message;
    }
}
