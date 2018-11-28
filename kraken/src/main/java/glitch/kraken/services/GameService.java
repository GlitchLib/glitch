package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.api.http.HttpRequest;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.Game;
import glitch.kraken.object.json.list.Games;
import reactor.core.publisher.Flux;

public class GameService extends AbstractHttpService {
    public GameService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Flux<Game> getTopGames(Integer limit, Integer offset) {
        HttpRequest<Games> request = get("/games/top", Games.class);

        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        return exchange(request).toFlux(OrdinalList::getData);
    }
}
