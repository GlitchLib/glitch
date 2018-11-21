package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.kraken.GlitchKraken;

public class StreamService extends AbstractHttpService {
    public StreamService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }
}
