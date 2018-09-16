package glitch.core.utils.http;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PACKAGE)
public class ResponseError {
    private String message;
    private int status;
    private String error;
}
