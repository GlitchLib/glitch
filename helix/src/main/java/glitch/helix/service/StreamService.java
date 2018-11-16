package glitch.helix.service;

import glitch.api.AbstractHttpService;
import glitch.helix.GlitchHelix;

public class StreamService extends AbstractHttpService {
    public StreamService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }
}
