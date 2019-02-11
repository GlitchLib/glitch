package glitch.helix.service;

import glitch.auth.objects.json.Credential;
import glitch.helix.GlitchHelix;
import glitch.helix.service.request.ExtensionAnalyticsRequest;
import glitch.helix.service.request.GameAnalyticsRequest;
import glitch.service.AbstractHttpService;

public class AnalyticsService extends AbstractHttpService {
    public AnalyticsService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }

    public ExtensionAnalyticsRequest getExtensionAnalytics(Credential credential) {
        return new ExtensionAnalyticsRequest(http, credential);
    }

    // TODO
    public GameAnalyticsRequest getGameAnalytics() {
        throw new UnsupportedOperationException("Game Analytics is not available yet.");
    }
}
