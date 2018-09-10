package glitch.kraken;

import glitch.GlitchClient;
import glitch.core.api.AbstractAPI;
import glitch.core.utils.api.BaseURL;
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

public class KrakenAPI extends AbstractAPI {
    public KrakenAPI(GlitchClient client) {
        super(client, client.createClient(KrakenAPI.class), BaseURL.create("https://api.twitch.tv/kraken"));
    }

    /**
     * @return
     */
    public BitsService bitsService() {
        return new BitsService(client, httpClient, baseURL);
    }

    /**
     * @return
     */
    public ChannelService channelService() {
        return new ChannelService(client, httpClient, baseURL);
    }

    /**
     * @return
     */
    public ChatService chatService() {
        return new ChatService(client, httpClient, baseURL);
    }

    /**
     * @return
     */
    public ClipService clipService() {
        return new ClipService(client, httpClient, baseURL);
    }

    /**
     * @return
     */
    public CollectionService colletionService() {
        return new CollectionService(client, httpClient, baseURL);
    }

    /**
     * @return
     */
    public CommunityService communitieService() {
        return new CommunityService(client, httpClient, baseURL);
    }

    /**
     * @return
     */
    public GameService gameService() {
        return new GameService(client, httpClient, baseURL);
    }

    /**
     * @return
     */
    public IngestService ingestService() {
        return new IngestService(client, httpClient, baseURL);
    }

    /**
     * @return
     */
    public SearchService searchService() {
        return new SearchService(client, httpClient, baseURL);
    }

    /**
     * @return
     */
    public StreamService streamService() {
        return new StreamService(client, httpClient, baseURL);
    }

    /**
     * @return
     */
    public TeamService teamService() {
        return new TeamService(client, httpClient, baseURL);
    }

    /**
     * @return
     */
    public UserService userService() {
        return new UserService(client, httpClient, baseURL);
    }

    /**
     * @return
     */
    public VideoService videoService() {
        return new VideoService(client, httpClient, baseURL);
    }

    /**
     * Getting channel feed.
     *
     * @throws UnsupportedOperationException Channel Feed has been deprecated
     * @deprecated Channel Feed and Pulse has been removed from the Twitch.
     */
    @Deprecated
    public void channelFeed() {
        throw new UnsupportedOperationException("Channel Feed has been deprecated");
    }
}
