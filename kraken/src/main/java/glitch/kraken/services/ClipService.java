package glitch.kraken.services;

import glitch.service.AbstractHttpService;
import glitch.auth.objects.json.Credential;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.Clip;
import glitch.kraken.object.json.list.Clips;
import glitch.kraken.services.request.FollowedClipsRequest;
import glitch.kraken.services.request.TopClipsRequest;
import reactor.core.publisher.Mono;

public class ClipService extends AbstractHttpService {
    public ClipService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Mono<Clip> getClip(String slug) {
        return exchange(get(String.format("/clips/%s", slug), Clip.class)).toMono();
    }

    public TopClipsRequest getTopClips() {
        return new TopClipsRequest(http, get("/clips/top", Clips.class));
    }

    public FollowedClipsRequest getFollowedClips(Credential credential) {
        return new FollowedClipsRequest(http, get("/clips/followed", Clips.class), credential);

    }
}
