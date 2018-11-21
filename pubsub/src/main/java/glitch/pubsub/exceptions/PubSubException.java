package glitch.pubsub.exceptions;

import glitch.exceptions.GlitchException;

public class PubSubException extends GlitchException {
    public PubSubException(String message, Throwable cause) {
        super(message, cause);
    }

    public PubSubException(String message) {
        super(message);
    }
}
