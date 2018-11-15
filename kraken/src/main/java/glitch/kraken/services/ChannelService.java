package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.auth.Credential;
import glitch.exceptions.http.ResponseException;
import glitch.kraken.GlitchKraken;
import glitch.kraken.json.Channel;
import reactor.core.publisher.Mono;

public class ChannelService extends AbstractHttpService {
    public ChannelService(GlitchKraken rest) {
        super(rest);
    }

    public Mono<Channel> getChannel(Credential credential) {
        return exchange(get("/channel", Channel.class).header("Authorization", "OAuth " + credential.getAccessToken())).toMono()
                .onErrorResume(ResponseException.class, t -> {
                    log.error("Cannot get channels from credentials", t);
                    log.debug("Fetching channel from User ID: {}", credential.getUserId());
                    return getChannel(credential.getUserId());
                });
    }

    public Mono<Channel> getChannel(Long id) {
        return exchange(get(String.format("/channels/%s", id), Channel.class)).toMono();
    }

//    public Mono<Channel> updateChannel(Credential credential, Consumer<ChannelData> data) {
//        return updateChannel(credential.getUserId(), credential, data);
//    }
//
//    public Mono<Channel> updateChannel(Long id, Credential credential, Consumer<ChannelData> data) {
//
//    }
}
