package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.kraken.GlitchKraken;

public class VideoService extends AbstractHttpService {
    public VideoService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }
}
