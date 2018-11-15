package glitch.exceptions.ws;

import glitch.api.ws.CloseStatus;
import glitch.exceptions.GlitchException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RemoteCloseException extends GlitchException {
    private final CloseStatus status;

    @Override
    public String getMessage() {
        return String.format("[%s] %s", status.getCode(), status.getReason());
    }
}
