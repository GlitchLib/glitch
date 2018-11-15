package glitch.exceptions;

public class GlitchException extends Exception {
    public GlitchException() {
        super();
    }

    public GlitchException(String message) {
        super(message);
    }

    public GlitchException(String message, Throwable cause) {
        super(message, cause);
    }


}
