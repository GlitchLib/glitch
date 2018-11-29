package glitch.kraken.services.request;

import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpRequest;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.enums.Direction;
import glitch.kraken.object.json.Follow;
import glitch.kraken.object.json.list.Followers;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Setter
@Accessors(chain = true, fluent = true)
public class ChannelFollowersRequest extends AbstractRequest<Followers, Follow> {
    private Integer limit;
    private Integer offset;
    private String cursor;
    private Direction direction;

    public ChannelFollowersRequest(GlitchHttpClient httpClient, HttpRequest<Followers> request) {
        super(httpClient, request);
    }

    @Override
    protected HttpResponse<Followers> exchange() {
        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        if (cursor != null) {
            request.queryParam("cursor", cursor);
        }

        if (direction != null) {
            request.queryParam("direction", direction.name().toLowerCase());
        }

        return httpClient.exchange(request);
    }

    public Mono<Followers> get() {
        return exchange().toMono();
    }

    public Flux<Follow> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
