package glitch.kraken.services.request;

import com.google.common.net.UrlEscapers;
import glitch.api.http.HttpClient;
import glitch.api.http.HttpMethod;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.Game;
import glitch.kraken.object.json.list.GamesSearch;
import glitch.service.AbstractRestService;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Setter
@Accessors(chain = true, fluent = true)
public class GamesSearchRequest extends AbstractRestService.AbstractRequest<GamesSearch, Game> {
    private Boolean live;

    public GamesSearchRequest(HttpClient http, String query) {
        super(http, http.create(HttpMethod.GET, "/search/channels", GamesSearch.class).queryParam("query", UrlEscapers.urlFormParameterEscaper().escape(query)));
    }

    @Override
    protected HttpResponse<GamesSearch> exchange() {
        if (live != null) {
            request.queryParam("live", live);
        }

        return httpClient.exchange(request);
    }

    @Override
    public Mono<GamesSearch> get() {
        return exchange().toMono();
    }

    @Override
    public Flux<Game> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
