package glitch.helix.service;

import glitch.api.AbstractHttpService;
import glitch.auth.Scope;
import glitch.auth.objects.json.Credential;
import glitch.exceptions.http.ScopeIsMissingException;
import glitch.helix.GlitchHelix;
import glitch.helix.object.json.ClipCreator;
import glitch.helix.object.json.Game;
import glitch.helix.object.json.User;
import glitch.helix.object.json.list.SingletonClipCreator;
import glitch.helix.service.request.ClipsRequest;
import reactor.core.publisher.Mono;

public class ClipsService extends AbstractHttpService {
    public ClipsService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }

    public Mono<ClipCreator> createClip(Credential credential, Long channelId, boolean hasDelay) {
        if (checkRequiredScope(credential.getScopes(), Scope.CLIPS_EDIT)) {
            return exchange(post("/clips", SingletonClipCreator.class)
                    .queryParam("broadcaster_id", channelId)
                    .queryParam("has_delay", hasDelay)
                    .header("Authorization", "Bearer " + credential.getAccessToken()))
                    .toMono(clips -> clips.getData().get(0));
        } else return Mono.error(new ScopeIsMissingException(Scope.CLIPS_EDIT));
    }

    public ClipsRequest getClips(String... id) {
        return new ClipsRequest(http, id);
    }

    public ClipsRequest getClips(Game... game) {
        return new ClipsRequest(http, game);
    }

    public ClipsRequest getClips(User... user) {
        return new ClipsRequest(http, user);
    }
}
