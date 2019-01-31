package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.kraken.object.enums.Direction;
import glitch.kraken.object.enums.Sorting;
import glitch.kraken.object.json.UserChannelFollow;
import glitch.kraken.object.json.collections.UserChannelFollows;
import glitch.service.AbstractRequest;
import reactor.core.publisher.Mono;

public class UserFollowsRequest extends AbstractRequest<UserChannelFollow, UserChannelFollows> {
    private Integer limit;
    private Integer offset;
    private Direction direction;
    private Sorting sortBy;

    public UserFollowsRequest(HttpClient http, Long id) {
        super(http, Routes.get("/users/%s/follows/channels").newRequest(id));
    }

    public UserFollowsRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public UserFollowsRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public UserFollowsRequest setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }

    public UserFollowsRequest setSortBy(Sorting sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    @Override
    public Mono<UserChannelFollows> get() {
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

        return httpClient.exchangeAs(request, UserChannelFollows.class);
    }
}
