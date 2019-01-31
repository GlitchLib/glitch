package glitch.kraken.services;

import glitch.kraken.GlitchKraken;
import glitch.service.AbstractHttpService;

public class CommunityService extends AbstractHttpService {
    public CommunityService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }
}
