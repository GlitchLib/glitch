package glitch;

import glitch.auth.Credential;
import io.reactivex.Single;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GlitchClient {
    private final Config config;

    public interface Builder {
        Builder botCredential(Consumer<Credential.Initializer> builder);

        Single<GlitchClient> build();
        Single<GlitchClient> login();
        GlitchClient buildAsync() throws Exception;
        GlitchClient loginAsync() throws Exception;
    }
}
