package glitch.helix.service.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.auth.GlitchScope;
import glitch.auth.objects.json.Credential;
import glitch.exceptions.http.ScopeIsMissingException;
import glitch.helix.object.HelixUtils;
import glitch.helix.object.enums.AnalyticsReportType;
import glitch.helix.object.json.Game;
import glitch.helix.object.json.GameAnalytics;
import glitch.helix.object.json.GameData;
import glitch.service.AbstractRequest;
import java.time.Instant;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class GameAnalyticsRequest extends AbstractRequest<GameData, GameAnalytics> {

    private final Credential credential;
    private String after;
    private Instant endedAt;
    private Integer first;
    private Game game;
    private Instant startedAt;
    private AnalyticsReportType type;

    public GameAnalyticsRequest(HttpClient httpClient, Credential credential) {
        super(httpClient, Routes.get("/analytics/games").newRequest());
        this.credential = credential;
    }

    public GameAnalyticsRequest setAfter(String after) {
        this.after = after;
        return this;
    }

    public GameAnalyticsRequest setEndedAt(Instant endedAt) {
        this.endedAt = endedAt;
        return this;
    }

    public GameAnalyticsRequest setFirst(Integer first) {
        this.first = first;
        return this;
    }

    public GameAnalyticsRequest setGame(Game game) {
        this.game = game;
        return this;
    }

    public GameAnalyticsRequest setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    public GameAnalyticsRequest setType(AnalyticsReportType type) {
        this.type = type;
        return this;
    }

    @Nonnull
    @Override
    public Mono<GameAnalytics> get() {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.ANALYTICS_READ_GAMES))
                .flatMap(scope -> {
                    if (scope) {
                        if (game != null) {
                            request.queryParam("game_id", game.getId());
                        } else if (after != null || first != null) {
                            if (after != null) {
                                request.queryParam("after", after);
                            }

                            if (first != null && (first > 0 && first <= 100)) {
                                request.queryParam("first", first);
                            }
                        }

                        if (endedAt != null) {
                            request.queryParam("ended_at", encodeQuery(HelixUtils.toRfc3339(endedAt)));
                        }

                        if (startedAt != null) {
                            request.queryParam("started_at", encodeQuery(HelixUtils.toRfc3339(startedAt)));
                        }

                        if (type != null) {
                            request.queryParam("type", type.name().toLowerCase());
                        }

                        return httpClient.exchangeAs(request.header("Authorization", "Bearer " + credential.getAccessToken()), GameAnalytics.class);
                    } else {
                        return Mono.error(new ScopeIsMissingException(GlitchScope.ANALYTICS_READ_GAMES));
                    }
                });
    }
}
