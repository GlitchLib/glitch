package glitch.helix.service.request;

import com.google.common.net.UrlEscapers;
import glitch.api.AbstractRequest;
import glitch.api.http.GlitchHttpClient;
import glitch.api.http.HttpMethod;
import glitch.api.http.HttpResponse;
import glitch.api.objects.json.interfaces.IDObject;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.exceptions.GlitchException;
import glitch.helix.object.HelixUtils;
import glitch.helix.object.json.Clip;
import glitch.helix.object.json.Game;
import glitch.helix.object.json.User;
import glitch.helix.object.json.list.Clips;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Setter
@Accessors(chain = true, fluent = true)
public class ClipsRequest extends AbstractRequest<Clips, Clip> {
    private final Collection<String> id = new LinkedHashSet<>();
    private final Collection<Long> gameId = new LinkedHashSet<>();
    private final Collection<Long> broadcasterId = new LinkedHashSet<>();

    private Long first;
    private String before;
    private String after;

    private Instant startedAt;
    private Instant endedAt;

    public ClipsRequest(GlitchHttpClient httpClient, Game[] game) {
        this(httpClient);
        this.gameId.addAll(Arrays.stream(Arrays.copyOf(game, 20)).map(IDObject::getId).collect(Collectors.toSet()));
    }

    public ClipsRequest(GlitchHttpClient httpClient, User[] user) {
        this(httpClient);
        this.broadcasterId.addAll(Arrays.stream(Arrays.copyOf(user, 20)).map(IDObject::getId).collect(Collectors.toSet()));
    }

    public ClipsRequest(GlitchHttpClient httpClient, String[] ids) {
        this(httpClient);
        this.id.addAll(Arrays.asList(Arrays.copyOf(ids, 100)));
    }

    ClipsRequest(GlitchHttpClient httpClient) {
        super(httpClient, httpClient.create(HttpMethod.GET, "/clips", Clips.class));
    }

    @Override
    protected HttpResponse<Clips> exchange() {
        if (startedAt != null) {
            request.queryParam("started_at", UrlEscapers.urlPathSegmentEscaper().escape(HelixUtils.toRfc3339(startedAt)));
            if (endedAt != null) {
                request.queryParam("ended_at", UrlEscapers.urlPathSegmentEscaper().escape(HelixUtils.toRfc3339(endedAt)));
            }
        }

        if (id.isEmpty() && (!gameId.isEmpty() || !broadcasterId.isEmpty())) {
            if (before != null) {
                request.queryParam("before", before);
            }

            if (after != null) {
                request.queryParam("after", after);
            }

            if (first != null) {
                request.queryParam("first", first);
            }

            if (!gameId.isEmpty()) {
                gameId.forEach(i -> request.queryParam("game_id", i));
            }

            if (!broadcasterId.isEmpty()) {
                broadcasterId.forEach(i -> request.queryParam("broadcaster_id", i));
            }
        }

        if (!id.isEmpty()) {
            broadcasterId.forEach(i -> request.queryParam("id", i));
        }

        return httpClient.exchange(request);
    }

    @Override
    public Mono<Clips> get() {
        if (before != null && after != null) {
            return Mono.error(new GlitchException("You must define one of pagination's word. \"#before()\" or \"#after()\""));
        } else {
            return exchange().toMono();
        }
    }

    @Override
    public Flux<Clip> getIterable() {
        return get().flatMapIterable(OrdinalList::getData);
    }
}
