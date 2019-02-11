package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.auth.objects.json.Credential;
import glitch.kraken.object.enums.Direction;
import glitch.kraken.object.json.Subscriber;
import glitch.kraken.object.json.collections.Subscribers;
import glitch.service.AbstractRequest;
import reactor.core.publisher.Mono;

public class ChannelSubscribersRequest extends AbstractRequest<Subscriber, Subscribers> {
    private final Credential credential;
    private Integer limit;
    private Integer offset;
    private Direction direction;

    public ChannelSubscribersRequest(HttpClient httpClient, Long id, Credential credential) {
        super(httpClient, Routes.get("/channels/%s/subscriptions").newRequest(id));
        this.credential = credential;
    }

    public ChannelSubscribersRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public ChannelSubscribersRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public ChannelSubscribersRequest setDirection(Direction direction) {
        this.direction = direction;
        return this;
    }

    @Override
    public Mono<Subscribers> get() {
        return null;
    }
}
