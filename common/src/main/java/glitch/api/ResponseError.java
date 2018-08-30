package glitch.api;

public class ResponseError {
    private String message;
    private int status;
    private String error;

    public ResponseError(String message, int status, String error) {
        this.message = message;
        this.status = status;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }


    void setMessage(String message) {
        this.message = message;
    }

    void setStatus(int status) {
        this.status = status;
    }

    void setError(String error) {
        this.error = error;
    }

}
