package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.kraken.GlitchKraken;

public class UserService extends AbstractHttpService {
    public UserService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }
}
