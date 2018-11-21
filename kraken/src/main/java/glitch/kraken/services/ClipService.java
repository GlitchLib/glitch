package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.kraken.GlitchKraken;

public class ClipService extends AbstractHttpService {
    public ClipService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }
}
