package glitch.kraken.services;

import glitch.GlitchClient;
import glitch.api.AbstractService;
import glitch.common.api.BaseURL;
import glitch.common.api.HttpClient;

public class ChannelService extends AbstractService {
    public ChannelService(GlitchClient client, HttpClient httpClient, BaseURL baseURL) {
        super(client, httpClient, baseURL);
    }
}
