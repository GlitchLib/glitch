package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.kraken.object.json.Game;
import glitch.kraken.object.json.collections.Games;
import glitch.service.AbstractRequest;
import reactor.core.publisher.Mono;

public class GamesRequest extends AbstractRequest<Game, Games> {
    private Integer limit;
    private Integer offset;

    public GamesRequest(HttpClient httpClient) {
        super(httpClient, Routes.get("/games/top").newRequest());
    }

    public GamesRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public GamesRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public Mono<Games> get() {
        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        return httpClient.exchangeAs(request, Games.class);
    }
}
