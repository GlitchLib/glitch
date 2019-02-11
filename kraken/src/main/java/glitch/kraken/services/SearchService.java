package glitch.kraken.services;

import glitch.kraken.GlitchKraken;
import glitch.kraken.services.request.ChannelSearchRequest;
import glitch.kraken.services.request.GamesSearchRequest;
import glitch.kraken.services.request.StreamSearchRequest;
import glitch.service.AbstractHttpService;

public class SearchService extends AbstractHttpService {
    public SearchService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public ChannelSearchRequest searchChannel(String query) {
        return new ChannelSearchRequest(http, query);
    }

    public GamesSearchRequest searchGames(String query) {
        return new GamesSearchRequest(http, query);
    }

    public StreamSearchRequest searchStreamss(String query) {
        return new StreamSearchRequest(http, query);
    }
}
