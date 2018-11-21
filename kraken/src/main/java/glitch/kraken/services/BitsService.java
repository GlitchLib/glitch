package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.kraken.GlitchKraken;

public class BitsService extends AbstractHttpService {
    public BitsService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

//    public HttpResponse<CheermoteList> getCheermotes() {
//        return exchange(get("/bits/actions", CheermoteList.class));
//    }

//    public HttpResponse<CheermoteList> getCheermotes(Long channelId) {
//        return exchange(get("/bits/actions", CheermoteList.class).queryParam("channel_id", channelId));
//    }
}
