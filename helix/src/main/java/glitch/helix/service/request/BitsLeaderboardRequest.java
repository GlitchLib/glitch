package glitch.helix.service.request;

import com.google.common.net.UrlEscapers;
import glitch.api.AbstractRequest;
import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpMethod;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.auth.Scope;
import glitch.auth.objects.json.Credential;
import glitch.exceptions.http.ScopeIsMissingException;
import glitch.helix.object.HelixUtils;
import glitch.helix.object.enums.Period;
import glitch.helix.object.json.Bits;
import glitch.helix.object.json.list.BitsLeaderboard;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Setter
@Accessors(chain = true, fluent = true)
public class BitsLeaderboardRequest extends AbstractRequest<BitsLeaderboard, Bits> {
    private final Credential credential;

    private Integer count;
    private Period period;
    private Instant startedAt;
    private Long userId;

    public BitsLeaderboardRequest(GlitchHttpClient http, Credential credential) {
        super(http, http.create(HttpMethod.GET, "/bits/leaderboard", BitsLeaderboard.class));
        this.credential = credential;
    }

    @Override
    protected HttpResponse<BitsLeaderboard> exchange() {
        if (userId != null) {
            request.queryParam("user_id", userId.toString());
        } else if (count > 0 && count <= 100) {
            request.queryParam("count", count);
        }

        if (period != null) {
            request.queryParam("period", period.name().toLowerCase());

            if (period != Period.ALL && startedAt != null) {
                request.queryParam("started_at", UrlEscapers.urlPathSegmentEscaper().escape(HelixUtils.toRfc3339(startedAt)));
            }
        }

        return httpClient.exchange(request.header("Authorization", "Bearer " + credential.getAccessToken()));
    }

    @Override
    public Mono<BitsLeaderboard> get() {
        if (checkRequiredScope(credential.getScopes(), Scope.BITS_READ)) {
            return exchange().toMono();
        } else return Mono.error(new ScopeIsMissingException(Scope.ANALYTICS_READ_EXTENSION));
    }

    @Override
    public Flux<Bits> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
