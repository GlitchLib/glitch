package glitch.chat.exceptions;

import glitch.exceptions.GlitchException;

public class NotJoinedChannelException extends GlitchException {
    public NotJoinedChannelException(String channel) {
        super("You are no longer joined to channel #" + channel);
    }
}
