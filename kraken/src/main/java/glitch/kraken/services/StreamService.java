package glitch.kraken.services;

import com.google.gson.JsonObject;
import glitch.api.http.HttpRequest;
import glitch.api.http.Routes;
import glitch.auth.objects.json.Credential;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.enums.StreamType;
import glitch.kraken.object.json.Game;
import glitch.kraken.object.json.Stream;
import glitch.kraken.object.json.StreamSummary;
import glitch.kraken.services.request.FeatureStreamsRequest;
import glitch.kraken.services.request.FollowedStreamRequest;
import glitch.kraken.services.request.LiveStreamsRequest;
import glitch.service.AbstractHttpService;
import reactor.core.publisher.Mono;


public class StreamService extends AbstractHttpService {
    public StreamService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Mono<Stream> getStreamByUser(Long id, StreamType streamType) {
        HttpRequest request = Routes.get("/streams/%s").newRequest(id);

        if (streamType != null) {
            request.queryParam("stream_type", streamType.name().toLowerCase());
        }

        return exchangeTo(request, JsonObject.class).map(object -> object.get("stream"))
                .flatMap(element -> {
                    if (element.isJsonNull()) {
                        return Mono.empty();
                    } else {
                        return Mono.just(http.getGson().fromJson(element, Stream.class));
                    }
                });
    }

    public LiveStreamsRequest getLiveStreams() {
        return new LiveStreamsRequest(http);
    }

    public Mono<StreamSummary> getStreamSummary(Game game) {
        HttpRequest request = Routes.get("/streams/summary").newRequest();

        if (game != null) {
            request.queryParam("game", game.getName());
        }

        return exchangeTo(request, StreamSummary.class);
    }

    public FeatureStreamsRequest getFeaturedStreams() {
        return new FeatureStreamsRequest(http);
    }

    public FollowedStreamRequest getFollowedStreams(Credential credential) {
        return new FollowedStreamRequest(http, credential);
    }
}
