package glitch.kraken;

import glitch.GlitchClient;
import glitch.service.AbstractRestService;
import glitch.api.http.HttpClient;
import glitch.kraken.services.*;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class GlitchKraken extends AbstractRestService {
    private static final String BASE_URL = "https://api.twitch.tv/kraken";

    private GlitchKraken(GlitchClient client, HttpClient http) {
        super(client, http);

        registerAllServices();
    }

    private void registerAllServices() {
        this.register(new BitsService(this));
        this.register(new ChannelService(this));
        this.register(new ChatService(this));
        this.register(new ClipService(this));
        this.register(new CollectionService(this));
        this.register(new CommunityService(this));
        this.register(new GameService(this));
        this.register(new IngestService(this));
        this.register(new SearchService(this));
        this.register(new StreamService(this));
        this.register(new TeamService(this));
        this.register(new UserService(this));
        this.register(new VideoService(this));
    }

    public static GlitchKraken create(GlitchClient client) {
        HttpClient httpClient = HttpClient.builder()
                .withBaseUrl(BASE_URL)
                .withDefaultTypeAdapters()
                .addTypeAdapters(krakenAdapters())
                .addHeader("Client-ID", client.getConfiguration().getClientId())
                .addHeader("Accept", "application/vnd.twitchtv.v5+json") // TODO: Removing after v3 will be removed
                .addHeader("User-Agent", client.getConfiguration().getUserAgent())
                .build();

        return new GlitchKraken(client, httpClient);
    }

    private static Map<Type, Object> krakenAdapters() {
        Map<Type, Object> adapters = new LinkedHashMap<>();

        return adapters;
    }
}
