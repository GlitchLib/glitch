package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.list.Games;
import glitch.kraken.services.request.GamesRequest;

public class GameService extends AbstractHttpService {
    public GameService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public GamesRequest getTopGames() {
        return new GamesRequest(http, get("/games/top", Games.class));
    }
}
