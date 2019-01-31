package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.auth.GlitchScope;
import glitch.auth.objects.json.Credential;
import glitch.kraken.object.json.Clip;
import glitch.kraken.object.json.collections.Clips;
import glitch.service.AbstractRequest;
import reactor.core.publisher.Mono;

public class FollowedClipsRequest extends AbstractRequest<Clip, Clips> {

    private final Credential credential;
    private String cursor;
    private Long limit;
    private Boolean trending;

    public FollowedClipsRequest(HttpClient http, Credential credential) {
        super(http, Routes.get("/clips/followed").newRequest());
        this.credential = credential;
    }

    public FollowedClipsRequest setCursor(String cursor) {
        this.cursor = cursor;
        return this;
    }

    public FollowedClipsRequest setLimit(Long limit) {
        this.limit = limit;
        return this;
    }

    public FollowedClipsRequest setTrending(Boolean trending) {
        this.trending = trending;
        return this;
    }

    @Override
    public Mono<Clips> get() {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.USER_READ)).flatMap(b -> {
            if (b) {
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

                return httpClient.exchangeAs(request, Clips.class);
            } else {
                return Mono.error(handleScopeMissing(GlitchScope.USER_READ));
            }
        });
    }
}
