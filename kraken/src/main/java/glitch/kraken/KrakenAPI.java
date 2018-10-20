package glitch.kraken;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import feign.Feign;
import glitch.GlitchClient;
import glitch.core.api.AbstractAPI;
import glitch.core.utils.GlitchUtils;
import glitch.core.utils.http.HTTP;
import glitch.core.utils.http.instances.KrakenInstance;
import glitch.kraken.services.*;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class KrakenAPI extends AbstractAPI {

    private KrakenAPI(Feign.Builder client) {
        super(client);
    }

    /**
     * @return
     */
    public BitsService bitsService() {
        return client.target(new KrakenInstance<>(BitsService.class));
    }

    /**
     * @return
     */
    public ChannelService channelService() {
        return client.target(new KrakenInstance<>(ChannelService.class));
    }

    /**
     * @return
     */
    public ChatService chatService() {
        return client.target(new KrakenInstance<>(ChatService.class));
    }

    /**
     * @return
     */
    public ClipService clipService() {
        return client.target(new KrakenInstance<>(ClipService.class));
    }

    /**
     * @return
     */
    public CollectionService colletionService() {
        return client.target(new KrakenInstance<>(CollectionService.class));
    }

    /**
     * @return
     */
    public CommunityService communitieService() {
        return client.target(new KrakenInstance<>(CommunityService.class));
    }

    /**
     * @return
     */
    public GameService gameService() {
        return client.target(new KrakenInstance<>(GameService.class));
    }

    /**
     * @return
     */
    public IngestService ingestService() {
        return client.target(new KrakenInstance<>(IngestService.class));
    }

    /**
     * @return
     */
    public SearchService searchService() {
        return client.target(new KrakenInstance<>(SearchService.class));
    }

    /**
     * @return
     */
    public StreamService streamService() {
        return client.target(new KrakenInstance<>(StreamService.class));
    }

    /**
     * @return
     */
    public TeamService teamService() {
        return client.target(new KrakenInstance<>(TeamService.class));
    }

    /**
     * @return
     */
    public UserService userService() {
        return client.target(new KrakenInstance<>(UserService.class));
    }

    /**
     * @return
     */
    public VideoService videoService() {
        return client.target(new KrakenInstance<>(VideoService.class));
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

        return new KrakenAPI(HTTP.create(headers, GlitchUtils.createGson(krakenAdapters(), true)));
    }

    private static Map<Type, Object> krakenAdapters() {
        Map<Type, Object> adapters = new LinkedHashMap<>();

        return adapters;
    }
}
