package glitch.kraken.services.request;

import glitch.api.AbstractRequest;
import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpRequest;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.auth.Scope;
import glitch.auth.objects.json.Credential;
import glitch.kraken.object.enums.Direction;
import glitch.kraken.object.json.Subscriber;
import glitch.kraken.object.json.list.Subscribers;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Setter
@Accessors(chain = true, fluent = true)
public class ChannelSubscribersRequest extends AbstractRequest<Subscribers, Subscriber> {
    private Integer limit;
    private Integer offset;
    private Direction direction;

    private final Credential credential;

    public ChannelSubscribersRequest(GlitchHttpClient httpClient, HttpRequest<Subscribers> request, Credential credential) {
        super(httpClient, request);
        this.credential = credential;
    }

    @Override
    protected HttpResponse<Subscribers> exchange() {
        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        if (direction != null) {
            request.queryParam("direction", direction.name().toLowerCase());
        }

        return httpClient.exchange(request);
    }

    @Override
    public Mono<Subscribers> get() {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.CHANNEL_SUBSCRIPTIONS))
                .flatMap(b -> {
                    if (b) {
                        return exchange().toMono();
                    } else {
                        return Mono.error(handleScopeMissing(Scope.CHANNEL_SUBSCRIPTIONS));
                    }
                });
    }

    @Override
    public Flux<Subscriber> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
