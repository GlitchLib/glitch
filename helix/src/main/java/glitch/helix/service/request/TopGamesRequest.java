package glitch.helix.service.request;

import glitch.api.AbstractRequest;
import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpMethod;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.exceptions.GlitchException;
import glitch.helix.object.json.Game;
import glitch.helix.object.json.list.Games;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Setter
@Accessors(chain = true, fluent = true)
public class TopGamesRequest extends AbstractRequest<Games, Game> {
    private Long first;
    private String before;
    private String after;

    public TopGamesRequest(GlitchHttpClient http) {
        super(http, http.create(HttpMethod.GET, "/games/top", Games.class));
    }

    @Override
    protected HttpResponse<Games> exchange() {
        if (first != null && first > 0 && first <= 100) {
            request.queryParam("first", first);
        }

        if (before != null) {
            request.queryParam("before", before);
        }

        if (after != null) {
            request.queryParam("after", after);
        }

        return httpClient.exchange(request);
    }

    @Override
    public Mono<Games> get() {
        if (before != null && after != null) {
            return Mono.error(new GlitchException("You must define one of pagination's word. \"#before()\" or \"#after()\""));
        } else {
            return exchange().toMono();
        }
    }

    @Override
    public Flux<Game> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
