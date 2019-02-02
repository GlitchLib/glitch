package glitch.helix.service;

import glitch.api.http.HttpRequest;
import glitch.api.http.Routes;
import glitch.auth.GlitchScope;
import glitch.auth.objects.json.Credential;
import glitch.exceptions.http.ScopeIsMissingException;
import glitch.helix.GlitchHelix;
import glitch.helix.object.json.ClipCreate;
import glitch.helix.object.json.Game;
import glitch.helix.object.json.SingleClipCreate;
import glitch.helix.object.json.User;
import glitch.helix.service.request.ClipsRequest;
import glitch.service.AbstractHttpService;
import reactor.core.publisher.Mono;

public class ClipsService extends AbstractHttpService {
    public ClipsService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }

    public Mono<ClipCreate> createClip(Credential credential, Long channelId, boolean hasDelay) {
        return Mono.just(checkRequiredScope(credential.getScopes(), GlitchScope.CLIPS_EDIT))
                .flatMap(scope -> {
                    if (scope) {
                        HttpRequest request = Routes.post("/clips").newRequest()
                                .header("Authorization", "Bearer " + credential.getAccessToken())
                                .queryParam("broadcaster_id", channelId);

                        if (hasDelay) {
                            request.queryParam("has_delay", "true");
                        }

                        return exchangeTo(request, SingleClipCreate.class).map(single -> single.getData().get(0));
                    } else return Mono.error(new ScopeIsMissingException(GlitchScope.CLIPS_EDIT));
                });
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
