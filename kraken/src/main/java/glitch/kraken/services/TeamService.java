package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.kraken.GlitchKraken;

public class TeamService extends AbstractHttpService {
    public TeamService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }
}
