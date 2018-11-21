package glitch.chat.exceptions;

import glitch.exceptions.GlitchException;

public class WhispersExceededException extends GlitchException {
    public WhispersExceededException(String message) {
        super(message);
    }
}
