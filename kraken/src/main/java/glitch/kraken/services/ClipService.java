package glitch.kraken.services;

import glitch.api.http.Routes;
import glitch.auth.objects.json.Credential;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.Clip;
import glitch.kraken.services.request.FollowedClipsRequest;
import glitch.kraken.services.request.TopClipsRequest;
import glitch.service.AbstractHttpService;
import reactor.core.publisher.Mono;

public class ClipService extends AbstractHttpService {
    public ClipService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Mono<Clip> getClip(String slug) {
        return exchangeTo(Routes.get("/clips/%s").newRequest(slug), Clip.class);
    }

    public TopClipsRequest getTopClips() {
        return new TopClipsRequest(http);
    }

    public FollowedClipsRequest getFollowedClips(Credential credential) {
        return new FollowedClipsRequest(http, credential);

    }
}
