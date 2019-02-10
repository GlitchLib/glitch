package glitch.chat.exceptions;

import glitch.exceptions.GlitchException;

public class AlreadyJoinedChannelException extends GlitchException {
    public AlreadyJoinedChannelException(String channel) {
        super("You already joined to channel: #" + channel);
    }
}
