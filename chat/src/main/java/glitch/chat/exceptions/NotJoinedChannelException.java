package glitch.chat.exceptions;

import glitch.exceptions.GlitchException;

public class NotJoinedChannelException extends GlitchException {
    public NotJoinedChannelException(String message) {
        super(message);
    }
}
