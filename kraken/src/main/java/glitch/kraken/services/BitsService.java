package glitch.kraken.services;

import glitch.service.AbstractHttpService;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.Cheermote;
import glitch.kraken.object.json.list.CheermoteList;
import reactor.core.publisher.Flux;

public class BitsService extends AbstractHttpService {
    public BitsService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Flux<Cheermote> getCheermotes() {
        return exchange(get("/bits/actions", CheermoteList.class))
                .toFlux(OrdinalList::getData);
    }

    public Flux<Cheermote> getCheermotes(Long channelId) {
        return exchange(get("/bits/actions", CheermoteList.class)
                .queryParam("channel_id", channelId))
                .toFlux(OrdinalList::getData);
    }
}
