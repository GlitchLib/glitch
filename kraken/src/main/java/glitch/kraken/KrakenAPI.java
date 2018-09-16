package glitch.kraken;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import glitch.GlitchClient;
import glitch.core.api.AbstractAPI;
import glitch.core.utils.GlitchUtils;
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
import java.util.LinkedHashMap;
import java.util.Map;
import retrofit2.Retrofit;

public class KrakenAPI extends AbstractAPI {

    private KrakenAPI(Retrofit retrofit) {
        super(retrofit);
    }

    /**
     * @return
     */
    public BitsService bitsService() {
        return retrofit.create(BitsService.class);
    }

    /**
     * @return
     */
    public ChannelService channelService() {
        return retrofit.create(ChannelService.class);
    }

    /**
     * @return
     */
    public ChatService chatService() {
        return retrofit.create(ChatService.class);
    }

    /**
     * @return
     */
    public ClipService clipService() {
        return retrofit.create(ClipService.class);
    }

    /**
     * @return
     */
    public CollectionService colletionService() {
        return retrofit.create(CollectionService.class);
    }

    /**
     * @return
     */
    public CommunityService communitieService() {
        return retrofit.create(CommunityService.class);
    }

    /**
     * @return
     */
    public GameService gameService() {
        return retrofit.create(GameService.class);
    }

    /**
     * @return
     */
    public IngestService ingestService() {
        return retrofit.create(IngestService.class);
    }

    /**
     * @return
     */
    public SearchService searchService() {
        return retrofit.create(SearchService.class);
    }

    /**
     * @return
     */
    public StreamService streamService() {
        return retrofit.create(StreamService.class);
    }

    /**
     * @return
     */
    public TeamService teamService() {
        return retrofit.create(TeamService.class);
    }

    /**
     * @return
     */
    public UserService userService() {
        return retrofit.create(UserService.class);
    }

    /**
     * @return
     */
    public VideoService videoService() {
        return retrofit.create(VideoService.class);
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


    public static KrakenAPI create(GlitchClient client) {
        Multimap<String, String> headers = LinkedHashMultimap.create();

        headers.put("Client-ID", client.getConfiguration().getClientId());
        headers.put("Accept", "application/vnd.twitchtv.v5+json"); // From 31.12.2018 v3 will be removed in new year. Will be not necessary soon.
        headers.put("User-Agent", client.getConfiguration().getUserAgent());

        return new KrakenAPI(GlitchUtils.createHttpClient(GlitchUtils.KRAKEN, headers, krakenSerializers(), krakenDeserializers()));
    }

    private static <X> Map<Class<X>, JsonDeserializer<X>> krakenDeserializers() {
        Map<Class<X>, JsonDeserializer<X>> deser = new LinkedHashMap<>();

        GlitchUtils.registerDeserializers(deser);

        return deser;
    }

    private static <X> Map<Class<X>, JsonSerializer<X>> krakenSerializers() {
        Map<Class<X>, JsonSerializer<X>> ser = new LinkedHashMap<>();

        GlitchUtils.registerSerializers(ser);

        return ser;
    }
}
