package glitch.core.utils.http;

import com.google.gson.Gson;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class GlitchErrorDecoder implements ErrorDecoder {
    private final Gson gson;

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            ResponseError error = gson.fromJson(response.body().asReader(), ResponseError.class);
            if (error.getError() == null || error.getError().equals("")) {
                error.setError(response.reason());
            }
            if (error.getMessage() == null || error.getMessage().equals("")) {
                error.setMessage(response.reason());
            }
            if (error.getStatus() == 0) {
                error.setStatus(response.status());
            }
            return new ResponseException(error);
        } catch (IOException e) {
            return e;
        }
    }
}
