package glitch.kraken.services;

import glitch.GlitchClient;
import glitch.auth.Credential;
import glitch.core.api.AbstractService;
import glitch.core.utils.api.BaseURL;
import glitch.core.utils.api.HttpClient;
import glitch.kraken.json.ChannelVerified;

public class ChannelService extends AbstractService {
    public ChannelService(GlitchClient client, HttpClient httpClient, BaseURL baseURL) {
        super(client, httpClient, baseURL);
    }

    public ChannelVerified getChannel(Credential credential) {
        return null;
    }
}
