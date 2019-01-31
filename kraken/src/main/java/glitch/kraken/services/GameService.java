package glitch.kraken.services;

import glitch.kraken.GlitchKraken;
import glitch.kraken.services.request.GamesRequest;
import glitch.service.AbstractHttpService;

public class GameService extends AbstractHttpService {
    public GameService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public GamesRequest getTopGames() {
        return new GamesRequest(http);
    }
}
