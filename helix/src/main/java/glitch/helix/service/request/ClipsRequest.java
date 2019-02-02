package glitch.helix.service.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.api.objects.json.interfaces.IDObject;
import glitch.exceptions.http.RequestException;
import glitch.helix.object.HelixUtils;
import glitch.helix.object.json.Clip;
import glitch.helix.object.json.Clips;
import glitch.helix.object.json.Game;
import glitch.helix.object.json.User;
import glitch.service.AbstractRequest;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import reactor.core.publisher.Mono;

public class ClipsRequest extends AbstractRequest<Clip, Clips> {
    private final Collection<String> id = new LinkedHashSet<>();
    private final Collection<Long> gameId = new LinkedHashSet<>();
    private final Collection<Long> broadcasterId = new LinkedHashSet<>();

    private Long first;
    private String before;
    private String after;

    private Instant startedAt;
    private Instant endedAt;

    public ClipsRequest(HttpClient httpClient, Game[] game) {
        this(httpClient);
        this.gameId.addAll(Arrays.stream(Arrays.copyOf(game, 20)).map(IDObject::getId).collect(Collectors.toSet()));
    }

    public ClipsRequest(HttpClient httpClient, User[] user) {
        this(httpClient);
        this.broadcasterId.addAll(Arrays.stream(Arrays.copyOf(user, 20)).map(IDObject::getId).collect(Collectors.toSet()));
    }

    public ClipsRequest(HttpClient httpClient, String[] ids) {
        this(httpClient);
        this.id.addAll(Arrays.asList(Arrays.copyOf(ids, 100)));
    }

    ClipsRequest(HttpClient httpClient) {
        super(httpClient, Routes.get("/clips").newRequest());
    }

    public ClipsRequest setFirst(Long first) {
        this.first = first;
        return this;
    }

    public ClipsRequest setBefore(String before) {
        this.before = before;
        this.after = null;
        return this;
    }

    public ClipsRequest setAfter(String after) {
        this.after = after;
        this.before = null;
        return this;
    }

    public ClipsRequest setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
        return this;
    }

    public ClipsRequest setEndedAt(Instant endedAt) {
        this.endedAt = endedAt;
        return this;
    }

    @Override
    public Mono<Clips> get() {
        if (!id.isEmpty()) {
            broadcasterId.forEach(i -> request.queryParam("id", i));
        } else if (!gameId.isEmpty() || !broadcasterId.isEmpty()) {
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

            if (startedAt != null) {
                request.queryParam("started_at", encodeQuery(HelixUtils.toRfc3339(startedAt)));
                if (endedAt != null) {
                    request.queryParam("ended_at", encodeQuery(HelixUtils.toRfc3339(endedAt)));
                }
            }
        } else {
            return Mono.error(new RequestException("You may specify only one of these parameters: id (one or more), broadcaster, or game"));
        }

        return httpClient.exchangeAs(request, Clips.class);
    }
}
