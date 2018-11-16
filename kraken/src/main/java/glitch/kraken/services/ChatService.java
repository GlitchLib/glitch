package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.kraken.GlitchKraken;

public class ChatService extends AbstractHttpService {
    public ChatService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }
}
