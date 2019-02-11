package glitch.helix.service;

import glitch.api.http.HttpRequest;
import glitch.api.http.Routes;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.helix.GlitchHelix;
import glitch.helix.object.json.Game;
import glitch.helix.object.json.Games;
import glitch.helix.service.request.TopGamesRequest;
import glitch.service.AbstractHttpService;
import java.util.Arrays;
import reactor.core.publisher.Flux;

public class GameService extends AbstractHttpService {
    public GameService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }

    public TopGamesRequest getTopGames() {
        return new TopGamesRequest(http);
    }

    public Flux<Game> getGamesById(Long... id) {
        HttpRequest request = Routes.get("/games").newRequest();

        Arrays.asList(Arrays.copyOf(id, 100))
                .forEach(i -> request.queryParam("id", i));

        return exchangeTo(request, Games.class).flatMapIterable(OrdinalList::getData);
    }

    public Flux<Game> getGamesByName(String... name) {
        HttpRequest request = Routes.get("/games").newRequest();

        Arrays.asList(Arrays.copyOf(name, 100))
                .forEach(n -> request.queryParam("name", n));

        return exchangeTo(request, Games.class).flatMapIterable(OrdinalList::getData);
    }
}
