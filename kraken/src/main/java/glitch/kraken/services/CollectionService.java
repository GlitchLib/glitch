package glitch.kraken.services;

import glitch.kraken.GlitchKraken;
import glitch.service.AbstractHttpService;

public class CollectionService extends AbstractHttpService {
    public CollectionService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }
}
