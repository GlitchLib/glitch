package glitch.exceptions.ws;

import glitch.exceptions.GlitchException;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class AlreadyConnectedException extends GlitchException {

    public AlreadyConnectedException(String message) {
        super(message);
    }

    public AlreadyConnectedException() {
        super();
    }
}
