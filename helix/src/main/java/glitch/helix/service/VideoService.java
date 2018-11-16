package glitch.helix.service;

import glitch.api.AbstractHttpService;
import glitch.helix.GlitchHelix;

public class VideoService extends AbstractHttpService {
    public VideoService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }
}
