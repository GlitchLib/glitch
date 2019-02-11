package glitch.chat.exceptions;

import glitch.exceptions.GlitchException;

public class WhispersExceededException extends GlitchException {
    public WhispersExceededException(int size) {
        super("Whispers daily exceeded! Check back tomorrow! [Max: " + size + "]");
    }
}
