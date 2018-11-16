package glitch.helix.service;

import glitch.api.AbstractHttpService;
import glitch.helix.GlitchHelix;

public class ClipsService extends AbstractHttpService {
    public ClipsService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }
}
