package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.kraken.object.json.Game;
import glitch.kraken.object.json.collections.Games;
import glitch.service.AbstractRequest;
import reactor.core.publisher.Mono;

public class GamesSearchRequest extends AbstractRequest<Game, Games> {
    private Boolean live;

    public GamesSearchRequest(HttpClient http, String query) {
        super(http, Routes.get("/search/channels").newRequest()
                .queryParam("query", encodeQuery(query)));
    }

    public GamesSearchRequest setLive(Boolean live) {
        this.live = live;
        return this;
    }

    @Override
    public Mono<Games> get() {

        if (live != null) {
            request.queryParam("live", live);
        }

        return httpClient.exchangeAs(request, Games.class);
    }
}
