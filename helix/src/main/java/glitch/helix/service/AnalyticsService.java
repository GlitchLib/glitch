package glitch.helix.service;

import glitch.service.AbstractHttpService;
import glitch.helix.GlitchHelix;

public class AnalyticsService extends AbstractHttpService {
    public AnalyticsService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }

    // TODO
    public void getExtensionAnalytics() {
        throw new UnsupportedOperationException("Extension Analytics is not available yet.");
    }

    // TODO
    public void getGameAnalytics() {
        throw new UnsupportedOperationException("Game Analytics is not available yet.");
    }
}
