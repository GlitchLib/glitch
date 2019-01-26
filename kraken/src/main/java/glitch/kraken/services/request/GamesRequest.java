package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.HttpRequest;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.Game;
import glitch.kraken.object.json.list.Games;
import glitch.service.AbstractRestService;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Setter
@Accessors(chain = true, fluent = true)
public class GamesRequest extends AbstractRestService.AbstractRequest<Games, Game> {
    private Integer limit;
    private Integer offset;

    public GamesRequest(HttpClient httpClient, HttpRequest<Games> request) {
        super(httpClient, request);
    }

    @Override
    protected HttpResponse<Games> exchange() {
        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        return httpClient.exchange(request);
    }

    @Override
    public Mono<Games> get() {
        return exchange().toMono();
    }

    @Override
    public Flux<Game> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
