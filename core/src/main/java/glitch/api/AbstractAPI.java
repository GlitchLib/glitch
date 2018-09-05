package glitch.api;

import glitch.GlitchClient;
import glitch.common.api.BaseURL;
import glitch.common.api.HttpClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractAPI {
    protected final GlitchClient client;
    protected final HttpClient httpClient;
    protected final BaseURL baseURL;


}
