package glitch.kraken.services;

import glitch.service.AbstractHttpService;
import glitch.kraken.GlitchKraken;

public class CommunityService extends AbstractHttpService {
    public CommunityService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }
}
