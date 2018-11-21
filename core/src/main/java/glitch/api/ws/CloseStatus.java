package glitch.api.ws;

import lombok.Data;

/**
 * Close Status POJO
 */
@Data
public class CloseStatus {
    private final int code;
    private final String reason;
}
