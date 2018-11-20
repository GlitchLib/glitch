package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.auth.objects.json.Credential;
import glitch.exceptions.http.ResponseException;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.AuthorizedChannel;
import glitch.kraken.object.json.Channel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;

public class ChannelService extends AbstractHttpService {
    public ChannelService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Mono<AuthorizedChannel> getChannel(Credential credential) {
        return exchange(get("/channel", AuthorizedChannel.class).header("Authorization", "OAuth " + credential.getAccessToken())).toMono();
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
