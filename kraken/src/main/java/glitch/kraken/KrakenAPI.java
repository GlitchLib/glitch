package glitch.kraken;

import glitch.common.api.BaseURL;
import glitch.common.api.HttpClient;
import glitch.kraken.services.BitsService;
import glitch.kraken.services.ChannelService;
import glitch.kraken.services.ChatService;
import glitch.kraken.services.ClipService;
import glitch.kraken.services.CollectionService;
import glitch.kraken.services.CommunityService;
import glitch.kraken.services.GameService;
import glitch.kraken.services.IngestService;
import glitch.kraken.services.SearchService;
import glitch.kraken.services.StreamService;
import glitch.kraken.services.TeamService;
import glitch.kraken.services.UserService;
import glitch.kraken.services.VideoService;

public class KrakenAPI {
    public static final BaseURL BASE = BaseURL.create("https://api.twitch.tv/kraken");
    private final HttpClient httpClient;

    public KrakenAPI(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public BitsService bits() { return new BitsService(BASE, httpClient); }
    public ChannelService channel() { return new ChannelService(BASE, httpClient); }
    public ChatService chat() { return new ChatService(BASE, httpClient); }
    public ClipService clips() { return new ClipService(BASE, httpClient); }
    public CollectionService colletions() { return new CollectionService(BASE, httpClient); }
    public CommunityService communities() { return new CommunityService(BASE, httpClient); }
    public GameService games() { return new GameService(BASE, httpClient); }
    public IngestService ingests() { return new IngestService(BASE, httpClient); }
    public SearchService search() { return new SearchService(BASE, httpClient); }
    public StreamService streams() { return new StreamService(BASE, httpClient); }
    public TeamService teams() { return new TeamService(BASE, httpClient); }
    public UserService users() { return new UserService(BASE, httpClient); }
    public VideoService videos() { return new VideoService(BASE, httpClient); }

    @Deprecated
    public Object channelFeed() { throw new UnsupportedOperationException("Channel Feed has been deprecated");}
}
