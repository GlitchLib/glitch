package glitch;

import io.reactivex.Single;

public interface Builder {
    Single<GlitchClient> build();
    Single<GlitchClient> login();
    GlitchClient buildAsync() throws Exception;
    GlitchClient loginAsync() throws Exception;
}
