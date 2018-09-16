package glitch.core.api;

import lombok.RequiredArgsConstructor;
import retrofit2.Retrofit;

@RequiredArgsConstructor
public abstract class AbstractAPI {
    protected final Retrofit retrofit;
}
