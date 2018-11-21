package glitch.helix.service;

import glitch.api.AbstractHttpService;
import glitch.helix.GlitchHelix;

public class WebhookService extends AbstractHttpService {
    public WebhookService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }
}
