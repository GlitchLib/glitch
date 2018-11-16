package glitch.helix.service;

import glitch.api.AbstractHttpService;
import glitch.helix.GlitchHelix;

public class EntitlementService extends AbstractHttpService {
    public EntitlementService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }
}
