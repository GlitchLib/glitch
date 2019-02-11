package glitch.helix.service.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.auth.GlitchScope;
import glitch.auth.objects.json.Credential;
import glitch.exceptions.http.ScopeIsMissingException;
import glitch.helix.object.HelixUtils;
import glitch.helix.object.enums.Period;
import glitch.helix.object.json.Bits;
import glitch.helix.object.json.BitsLeaderboard;
import glitch.helix.object.json.User;
import glitch.service.AbstractRequest;
import java.time.Instant;
import reactor.core.publisher.Mono;

public class BitsLeaderboardRequest extends AbstractRequest<Bits, BitsLeaderboard> {
    private final Credential credential;

    private Integer count;
    private Period period;
    private Instant startedAt;
    private User user;

    public BitsLeaderboardRequest(HttpClient http, Credential credential) {
        super(http, Routes.get("/bits/leaderboard").newRequest());
        this.credential = credential;
    }

    public BitsLeaderboardRequest setCount(Integer count) {
        this.count = count;
        return this;
    }

    public BitsLeaderboardRequest setPeriod(Period period) {
        this.period = period;
        return this;
    }

    public BitsLeaderboardRequest setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    public BitsLeaderboardRequest setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public Mono<BitsLeaderboard> get() {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.BITS_READ))
                .flatMap(scope -> {
                    if (scope) {
                        if (user != null) {
                            request.queryParam("user_id", user.getId().toString());
                        } else if (count != null && (count > 0 && count <= 100)) {
                            request.queryParam("count", count);
                        }

                        if (period != null) {
                            request.queryParam("period", period.name().toLowerCase());

                            if (period != Period.ALL && startedAt != null) {
                                request.queryParam("started_at", encodeQuery(HelixUtils.toRfc3339(startedAt)));
                            }
                        }

                        return httpClient.exchangeAs(request.header("Authorization", "Bearer " + credential.getAccessToken()), BitsLeaderboard.class);
                    } else return Mono.error(new ScopeIsMissingException(GlitchScope.BITS_READ));
                });
    }
}
