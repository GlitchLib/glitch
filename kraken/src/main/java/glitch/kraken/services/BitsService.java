package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.api.http.HttpResponse;
import glitch.kraken.GlitchKraken;
import glitch.kraken.json.lists.CheermoteList;

public class BitsService extends AbstractHttpService {
    public BitsService(GlitchKraken rest) {
        super(rest);
    }

    public HttpResponse<CheermoteList> getCheermotes() {
        return exchange(get("/bits/actions", CheermoteList.class));
    }

    public HttpResponse<CheermoteList> getCheermotes(Long channelId) {
        return exchange(get("/bits/actions", CheermoteList.class).queryParam("channel_id", channelId));
    }
}
