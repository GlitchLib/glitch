package glitch.kraken.services.request;

import glitch.api.AbstractRequest;
import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpMethod;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.Channel;
import glitch.kraken.object.json.list.Channels;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Setter
@Accessors(chain = true, fluent = true)
public class ChannelSearchRequest extends AbstractRequest<Channels, Channel> {
    private Integer offset;
    private Integer limit;

    public ChannelSearchRequest(GlitchHttpClient http, String query) {
        super(http, http.create(HttpMethod.GET, "/search/channels", Channels.class).queryParam("query", query));
    }

    @Override
    protected HttpResponse<Channels> exchange() {
        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        return httpClient.exchange(request);
    }

    @Override
    public Mono<Channels> get() {
        return exchange().toMono();
    }

    @Override
    public Flux<Channel> getIterable() {
        return exchange().toFlux(OrdinalList::getData);
    }
}
