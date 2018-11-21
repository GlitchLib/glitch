package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.kraken.GlitchKraken;

public class CollectionService extends AbstractHttpService {
    public CollectionService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }
}
