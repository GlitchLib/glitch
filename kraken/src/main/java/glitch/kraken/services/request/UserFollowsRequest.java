package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.HttpMethod;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.enums.Direction;
import glitch.kraken.object.enums.Sorting;
import glitch.kraken.object.json.UserFollow;
import glitch.kraken.object.json.list.UserFollowers;
import glitch.service.AbstractRestService;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Setter
@Accessors(chain = true, fluent = true)
public class UserFollowsRequest extends AbstractRestService.AbstractRequest<UserFollowers, UserFollow> {
    private Integer limit;
    private Integer offset;
    private Direction direction;
    private Sorting sortBy;

    public UserFollowsRequest(HttpClient http, Long id) {
        super(http, http.create(HttpMethod.GET, String.format("/users/%s/follows/channels", id), UserFollowers.class));
    }

    @Override
    protected HttpResponse<UserFollowers> exchange() {
        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        if (direction != null) {
            request.queryParam("direction", direction.name().toLowerCase());
        }

        if (sortBy != null) {
            request.queryParam("sortby", sortBy.name().toLowerCase());
        }

        return httpClient.exchange(request);
    }

    @Override
    public Mono<UserFollowers> get() {
        return exchange().toMono();
    }

    @Override
    public Flux<UserFollow> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
