package glitch.helix.service;

import glitch.service.AbstractHttpService;
import glitch.api.http.HttpRequest;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.helix.GlitchHelix;
import glitch.helix.object.json.Game;
import glitch.helix.object.json.list.Games;
import glitch.helix.service.request.TopGamesRequest;
import reactor.core.publisher.Flux;

import java.util.Arrays;

public class GameService extends AbstractHttpService {
    public GameService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }

    public Flux<Game> getGames(Long... id) {
        HttpRequest<Games> request = get("/games", Games.class);

        Arrays.asList(Arrays.copyOf(id, 100))
                .forEach(i -> request.queryParam("id", i));

        return exchange(request).toFlux(OrdinalList::getData);
    }

    public Flux<Game> getGames(String... name) {
        HttpRequest<Games> request = get("/games", Games.class);

        Arrays.asList(Arrays.copyOf(name, 100))
                .forEach(n -> request.queryParam("name", n));

        return exchange(request).toFlux(OrdinalList::getData);
    }

    public TopGamesRequest getTopGames() {
        return new TopGamesRequest(http);
    }
}
