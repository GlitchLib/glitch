package glitch.kraken.services;

import com.google.gson.JsonObject;
import glitch.api.AbstractHttpService;
import glitch.auth.Scope;
import glitch.auth.objects.json.Credential;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.Video;
import glitch.kraken.object.json.list.Videos;
import glitch.kraken.object.json.requests.VideoBodyData;
import glitch.kraken.services.request.FollowedVideosRequest;
import glitch.kraken.services.request.TopVideosRequest;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

public class VideoService extends AbstractHttpService {
    public VideoService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Mono<Video> getVideo(Long id) {
        return exchange(get(String.format("/videos/%s", id), Video.class)).toMono();
    }

    public TopVideosRequest getTopVideos() {
        return new TopVideosRequest(http, get("/videos/top", Videos.class));
    }

    public FollowedVideosRequest getTopVideos(Credential credential) {
        return new FollowedVideosRequest(http, get("/videos/followed", Videos.class), credential);
    }

    public void createUpload() {
        throw new UnsupportedOperationException("Uploading videos is currently not supported. Checkout GitHub feature issues: https://github.com/GlitchLib/glitch/issues/25");
    }

    public Mono<Video> updateVideo(Long id, Credential credential, Consumer<VideoBodyData> requestBody) {
        VideoBodyData bodyData = new VideoBodyData();
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.CHANNEL_EDITOR))
                .flatMap(b -> {
                    if (b) {
                        requestBody.accept(bodyData);
                        return exchange(put(String.format("/videos/%s", id), Video.class)
                                .body(bodyData)
                                .queryParam("Authorization", "OAuth " + credential.getAccessToken()))
                                .toMono();
                    } else {
                        return Mono.error(handleScopeMissing(Scope.CHANNEL_EDITOR));
                    }
                });
    }

    public Mono<Boolean> delete(Video video, Credential credential) {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.CHANNEL_EDITOR))
                .flatMap(b -> {
                    if (b) {
                        return exchange(delete(String.format("/videos/%s", video.getId()), JsonObject.class)
                                .queryParam("Authorization", "OAuth " + credential.getAccessToken()))
                                .toMono(o -> o.get("ok").getAsBoolean());
                    } else {
                        return Mono.error(handleScopeMissing(Scope.CHANNEL_EDITOR));
                    }
                });
    }
}
