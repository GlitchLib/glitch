package glitch.kraken.services;

import glitch.GlitchClient;
import glitch.auth.Credential;
import glitch.auth.Scope;
import glitch.auth.ScopeIsMissingException;
import glitch.core.api.AbstractService;
import glitch.core.utils.api.BaseURL;
import glitch.core.utils.api.HttpClient;
import glitch.core.utils.api.HttpMethod;
import glitch.core.utils.api.Router;
import glitch.kraken.json.Channel;
import glitch.kraken.json.ChannelVerified;
import io.reactivex.Single;

public class ChannelService extends AbstractService {
    public ChannelService(GlitchClient client, HttpClient httpClient, BaseURL baseURL) {
        super(client, httpClient, baseURL);
    }

    public Single<ChannelVerified> getChannel(Credential credential) {
        if (!hasRequiredScope(credential, Scope.CHANNEL_READ)) {
            return Single.error(new ScopeIsMissingException(Scope.CHANNEL_READ));
        }
        return Router.create(HttpMethod.GET, baseURL.endpoint("/channel"), ChannelVerified.class)
                .request()
                .header("Authorization", authorization("OAuth", credential))
                .exchange(httpClient);
    }

    public Single<Channel> getChannel(Long id) {
        return Router.create(HttpMethod.GET, baseURL.endpoint("/channels/%s"), Channel.class)
                .request(id)
                .exchange(httpClient);
    }
}
