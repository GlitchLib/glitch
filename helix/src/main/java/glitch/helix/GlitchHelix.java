package glitch.helix;

import glitch.GlitchClient;
import glitch.api.AbstractRestService;
import glitch.api.http.GlitchHttpClient;
import glitch.helix.service.*;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class GlitchHelix extends AbstractRestService {
    private static final String BASE_URL = "https://api.twitch.tv/helix";

    private GlitchHelix(GlitchClient client, GlitchHttpClient http) {
        super(client, http);

        registerAllServices();
    }

    @SuppressWarnings("unchecked")
    private void registerAllServices() {
        this.register(new AnalyticsService(this));
        this.register(new BitsService(this));
        this.register(new ClipsService(this));
        this.register(new EntitlementService(this));
        this.register(new GameService(this));
        this.register(new StreamService(this));
        this.register(new UserService(this));
        this.register(new VideoService(this));
        this.register(new WebhookService(this));
    }

    public static GlitchHelix create(GlitchClient client) {
        GlitchHttpClient http = GlitchHttpClient.builder()
                .withBaseUrl(BASE_URL)
                .withDefaultTypeAdapters()
                .addTypeAdapters(helixAdapters())
                .addHeader("Client-ID", client.getConfiguration().getClientId())
                .addHeader("User-Agent", client.getConfiguration().getUserAgent())
                .build();

        return new GlitchHelix(client, http);
    }

    private static Map<Type, Object> helixAdapters() {
        Map<Type, Object> adapters = new LinkedHashMap<>();

        return adapters;
    }
}
