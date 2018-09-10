package glitch.core.api;

import glitch.GlitchClient;
import glitch.core.utils.api.BaseURL;
import glitch.core.utils.api.HttpClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractAPI {
    protected final GlitchClient client;
    protected final HttpClient httpClient;
    protected final BaseURL baseURL;


}
