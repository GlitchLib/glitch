package glitch.core.api;

import feign.Feign;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractAPI {
    protected final Feign client;
}
