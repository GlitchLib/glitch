package glitch.chat.exceptions;

import glitch.exceptions.GlitchException;

public class AlreadyJoinedChannelException extends GlitchException {
    public AlreadyJoinedChannelException(String message) {
        super(message);
    }
}
