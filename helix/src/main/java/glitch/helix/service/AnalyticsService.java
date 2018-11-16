package glitch.helix.service;

import glitch.api.AbstractHttpService;
import glitch.helix.GlitchHelix;

public class AnalyticsService extends AbstractHttpService {
    public AnalyticsService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }
}
