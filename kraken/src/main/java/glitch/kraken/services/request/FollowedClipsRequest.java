package glitch.kraken.services.request;

import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpRequest;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.auth.Scope;
import glitch.auth.objects.json.Credential;
import glitch.kraken.object.json.Clip;
import glitch.kraken.object.json.list.Clips;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Setter
@Accessors(chain = true, fluent = true)
public class FollowedClipsRequest extends AbstractRequest<Clips, Clip> {

    private String cursor;
    private Long limit;
    private Boolean trending;

    private final Credential credential;

    public FollowedClipsRequest(GlitchHttpClient http, HttpRequest<Clips> request, Credential credential) {
        super(http, request);
        this.credential = credential;
    }

    @Override
    protected HttpResponse<Clips> exchange() {
        request.header("Authroization", "OAuth " + credential.getAccessToken());

        if (cursor != null) {
            request.queryParam("cursor", cursor);
        }

        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (trending != null) {
            request.queryParam("trending", trending);
        }

        return httpClient.exchange(request);
    }

    @Override
    public Mono<Clips> get() {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.USER_READ)).flatMap(b -> {
            if (b) {
                return exchange().toMono();
            } else {
                return Mono.error(handleScopeMissing(Scope.USER_READ));
            }
        });
    }

    @Override
    public Flux<Clip> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
