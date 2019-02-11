package glitch.exceptions.ws;

import glitch.exceptions.GlitchException;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class NotConnectedException extends GlitchException {

    public NotConnectedException(String message) {
        super(message);
    }

    public NotConnectedException() {
        super();
    }
}
