package glitch.exceptions;

public class GlitchException extends RuntimeException {
    public GlitchException() {
        super();
    }

    public GlitchException(String message) {
        super(message);
    }

    public GlitchException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlitchException(Throwable cause) {
        super(cause);
    }
}
