package glitch.helix.service.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.auth.GlitchScope;
import glitch.auth.objects.json.Credential;
import glitch.exceptions.http.ScopeIsMissingException;
import glitch.helix.object.HelixUtils;
import glitch.helix.object.enums.AnalyticsReportType;
import glitch.helix.object.json.ExtensionAnalytics;
import glitch.helix.object.json.ExtensionReport;
import glitch.service.AbstractRequest;
import java.time.Instant;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class ExtensionAnalyticsRequest extends AbstractRequest<ExtensionReport, ExtensionAnalytics> {

    private final Credential credential;
    private String after;
    private Instant endedAt;
    private String extensionId;
    private Integer first;
    private Instant startedAt;
    private AnalyticsReportType type;

    public ExtensionAnalyticsRequest(HttpClient httpClient, Credential credential) {
        super(httpClient, Routes.get("/analytics/extensions").newRequest());
        this.credential = credential;
    }

    public ExtensionAnalyticsRequest setAfter(String after) {
        this.after = after;
        return this;
    }

    public ExtensionAnalyticsRequest setEndedAt(Instant endedAt) {
        this.endedAt = endedAt;
        return this;
    }

    public ExtensionAnalyticsRequest setExtensionId(String extensionId) {
        this.extensionId = extensionId;
        return this;
    }

    public ExtensionAnalyticsRequest setFirst(Integer first) {
        this.first = first;
        return this;
    }

    public ExtensionAnalyticsRequest setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    public ExtensionAnalyticsRequest setType(AnalyticsReportType type) {
        this.type = type;
        return this;
    }

    @Nonnull
    @Override
    public Mono<ExtensionAnalytics> get() {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.ANALYTICS_READ_EXTENSION))
                .flatMap(scope -> {
                    if (scope) {
                        if (extensionId != null) {
                            request.queryParam("extension_id", extensionId);
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

                        return httpClient.exchangeAs(request.header("Authorization", "Bearer " + credential.getAccessToken()), ExtensionAnalytics.class);
                    } else {
                        return Mono.error(new ScopeIsMissingException(GlitchScope.ANALYTICS_READ_EXTENSION));
                    }
                });
    }
}
