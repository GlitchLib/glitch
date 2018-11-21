package glitch.exceptions.ws;

import glitch.api.ws.CloseStatus;
import glitch.exceptions.GlitchException;
import lombok.Getter;

@Getter
public class ClosedByRemoteException extends GlitchException {
    private final CloseStatus status;

    public ClosedByRemoteException(CloseStatus status) {
        super(String.format("[%s] %s", status.getCode(), status.getReason()));
        this.status = status;
    }
}
