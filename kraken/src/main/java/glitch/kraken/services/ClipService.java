package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.api.http.HttpRequest;
import glitch.auth.Scope;
import glitch.auth.objects.json.Credential;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.enums.ClipPeriod;
import glitch.kraken.object.json.Channel;
import glitch.kraken.object.json.Clip;
import glitch.kraken.object.json.Game;
import glitch.kraken.object.json.list.Clips;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

public class ClipService extends AbstractHttpService {
    public ClipService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Mono<Clip> getClip(String slug) {
        return exchange(get(String.format("/clips/%s", slug), Clip.class)).toMono();
    }

    public Mono<Clips> getTopClips(Channel channel, Game game, String cursor, Collection<Locale> language, Long limit, ClipPeriod period, Boolean trending) {

        HttpRequest<Clips> request = get("/clips/top", Clips.class);

        if (channel != null) {
            request.queryParam("channel", channel.getUsername());
        } else if (game != null) {
            request.queryParam("game", game.getName());
        }

        if (cursor != null) {
            request.queryParam("cursor", cursor);
        }

        if (language != null && !language.isEmpty() && language.size() <= 28) {
            request.queryParam("language", language.stream().map(Locale::getLanguage).collect(Collectors.joining(",")));
        }

        if (limit != null && limit > -1 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (period != null) {
            request.queryParam("period", period.name().toLowerCase());
        }

        if (trending != null) {
            request.queryParam("trending", trending);
        }

        return exchange(request).toMono();
    }

    public Mono<Clips> getFollowedClips(Credential credential, String cursor, Long limit, Boolean trending) {
        return Mono.just(checkRequiredScope(credential.getScopes(), Scope.USER_READ)).flatMap(b -> {
            if (b) {
                HttpRequest<Clips> request = get("/clips/followed", Clips.class)
                        .header("Authroization", "OAuth " + credential.getAccessToken());

                if (cursor != null) {
                    request.queryParam("cursor", cursor);
                }

                if (limit != null && limit > -1 && limit <= 100) {
                    request.queryParam("limit", limit);
                }

                if (trending != null) {
                    request.queryParam("trending", trending);
                }

                return exchange(request).toMono();
            } else {
                return Mono.error(handleScopeMissing(Scope.USER_READ));
            }
        });
    }
}
