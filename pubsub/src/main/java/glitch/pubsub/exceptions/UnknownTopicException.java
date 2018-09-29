package glitch.pubsub.exceptions;

import java.io.IOException;

public class UnknownTopicException extends IOException {
    public UnknownTopicException(String message) {
        super(message);
    }
}
