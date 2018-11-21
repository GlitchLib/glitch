package glitch.helix.service;

import glitch.api.AbstractHttpService;
import glitch.helix.GlitchHelix;

public class BitsService extends AbstractHttpService {
    public BitsService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }
}
