package glitch.helix.service.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.helix.object.json.Game;
import glitch.helix.object.json.Games;
import glitch.service.AbstractRequest;
import reactor.core.publisher.Mono;

public class TopGamesRequest extends AbstractRequest<Game, Games> {
    private Long first;
    private String before;
    private String after;

    public TopGamesRequest(HttpClient http) {
        super(http, Routes.get("/games/top").newRequest());
    }

    public TopGamesRequest setFirst(Long first) {
        this.first = first;
        return this;
    }

    public TopGamesRequest setBefore(String before) {
        this.before = before;
        this.after = null;
        return this;
    }

    public TopGamesRequest setAfter(String after) {
        this.after = after;
        this.before = null;
        return this;
    }

    @Override
    public Mono<Games> get() {
        if (first != null && first > 0 && first <= 100) {
            request.queryParam("first", first);
        }

        if (before != null) {
            request.queryParam("before", before);
        }

        if (after != null) {
            request.queryParam("after", after);
        }

        return httpClient.exchangeAs(request, Games.class);
    }
}
