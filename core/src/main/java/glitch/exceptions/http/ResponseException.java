package glitch.exceptions.http;

import glitch.exceptions.GlitchException;

public class ResponseException extends GlitchException {
    private final int status;
    private final String error;
    private final String message;

    public ResponseException(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
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

    @Override
    public String getMessage() {
        return String.format("[%s] %s", status, (message != null && !message.equals("")) ? message : error);
    }
}
