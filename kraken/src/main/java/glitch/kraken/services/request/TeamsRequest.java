package glitch.kraken.services.request;

import glitch.api.AbstractRequest;
import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpRequest;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.Team;
import glitch.kraken.object.json.list.Teams;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Setter
@Accessors(chain = true, fluent = true)
public class TeamsRequest extends AbstractRequest<Teams, Team> {
    private Integer limit;
    private Integer offset;

    public TeamsRequest(GlitchHttpClient httpClient, HttpRequest<Teams> request) {
        super(httpClient, request);
    }

    @Override
    protected HttpResponse<Teams> exchange() {
        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        return httpClient.exchange(request);
    }

    @Override
    public Mono<Teams> get() {
        return exchange().toMono();
    }

    @Override
    public Flux<Team> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
