package glitch.helix.service;

import glitch.api.AbstractHttpService;
import glitch.helix.GlitchHelix;

public class GameService extends AbstractHttpService {
    public GameService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }
}
