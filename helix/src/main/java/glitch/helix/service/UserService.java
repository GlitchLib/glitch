package glitch.helix.service;

import glitch.api.AbstractHttpService;
import glitch.helix.GlitchHelix;

public class UserService extends AbstractHttpService {
    public UserService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }
}
