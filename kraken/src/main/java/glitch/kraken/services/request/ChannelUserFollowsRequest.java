package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.kraken.object.enums.Direction;
import glitch.kraken.object.json.ChannelUserFollow;
import glitch.kraken.object.json.collections.ChannelUserFollows;
import glitch.service.AbstractRequest;
import reactor.core.publisher.Mono;

public class ChannelUserFollowsRequest extends AbstractRequest<ChannelUserFollow, ChannelUserFollows> {
    private Integer limit;
    private Integer offset;
    private String cursor;
    private Direction direction;

    public ChannelUserFollowsRequest(HttpClient httpClient, Long id) {
        super(httpClient, Routes.get("/channels/%s/follows").newRequest(id));
    }

    public ChannelUserFollowsRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public ChannelUserFollowsRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public ChannelUserFollowsRequest setCursor(String cursor) {
        this.cursor = cursor;
        return this;
    }

    public ChannelUserFollowsRequest setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }

    @Override
    public Mono<ChannelUserFollows> get() {

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

        return httpClient.exchangeAs(request, ChannelUserFollows.class);
    }
}
