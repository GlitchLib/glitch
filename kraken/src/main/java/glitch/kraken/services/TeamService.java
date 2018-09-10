package glitch.kraken.services;

import glitch.GlitchClient;
import glitch.core.api.AbstractService;
import glitch.core.utils.api.BaseURL;
import glitch.core.utils.api.HttpClient;

public class TeamService extends AbstractService {
    public TeamService(GlitchClient client, HttpClient httpClient, BaseURL baseURL) {
        super(client, httpClient, baseURL);
    }
}
