package glitch.kraken.services;

import com.google.gson.JsonObject;
import glitch.api.http.HttpRequest;
import glitch.api.http.Routes;
import glitch.auth.GlitchScope;
import glitch.auth.objects.json.Credential;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.Video;
import glitch.kraken.object.json.VideoBody;
import glitch.kraken.services.request.FollowedVideosRequest;
import glitch.kraken.services.request.TopVideosRequest;
import glitch.service.AbstractHttpService;
import java.util.Objects;
import reactor.core.publisher.Mono;

public class VideoService extends AbstractHttpService {
    public VideoService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Mono<Video> getVideo(Long id) {
        return exchange(Routes.get("/videos/%s").newRequest(id)).map(response -> response.getBodyAs(Video.class));
    }

    public TopVideosRequest getTopVideos() {
        return new TopVideosRequest(http);
    }

    public FollowedVideosRequest getTopVideos(Credential credential) {
        return new FollowedVideosRequest(http, credential);
    }

    public void createUpload(Credential credential) {
        throw new UnsupportedOperationException("Uploading videos is currently not supported. Checkout GitHub feature issues: https://github.com/GlitchLib/glitch/issues/25");
    }

    public Mono<Video> updateVideo(Long id, Credential credential, VideoBody body) {

        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.CHANNEL_EDITOR))
                .flatMap(b -> {
                    if (b) {
                        return exchange(Routes.put("/videos/%s").newRequest(id)
                                .body(HttpRequest.BodyType.JSON, body)
                                .queryParam("Authorization", "OAuth " + credential.getAccessToken()))
                                .map(response -> response.getBodyAs(Video.class));
                    } else {
                        return Mono.error(handleScopeMissing(GlitchScope.CHANNEL_EDITOR));
                    }
                });
    }

    public Mono<Boolean> delete(Video video, Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.CHANNEL_EDITOR))
                .flatMap(b -> {
                    if (b) {
                        return exchange(Routes.delete("/videos/%s").newRequest(video.getId())
                                .queryParam("Authorization", "OAuth " + credential.getAccessToken()))
                                .map(response -> Objects.requireNonNull(response.getBodyAs(JsonObject.class)).getAsJsonPrimitive("ok").getAsBoolean());
                    } else {
                        return Mono.error(handleScopeMissing(GlitchScope.CHANNEL_EDITOR));
                    }
                });
    }
}
