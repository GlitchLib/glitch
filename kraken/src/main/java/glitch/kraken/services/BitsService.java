package glitch.kraken.services;

import glitch.api.http.Routes;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.Cheermote;
import glitch.kraken.object.json.collections.Cheermotes;
import glitch.service.AbstractHttpService;
import reactor.core.publisher.Flux;

public class BitsService extends AbstractHttpService {
    public BitsService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Flux<Cheermote> getCheermotes() {
        return exchangeTo(Routes.get("/bits/actions").newRequest(), Cheermotes.class)
                .flatMapIterable(OrdinalList::getData);
    }

    public Flux<Cheermote> getCheermotes(Long channelId) {
        return exchangeTo(Routes.get("/bits/actions").newRequest().queryParam("channel_id", channelId), Cheermotes.class)
                .flatMapIterable(OrdinalList::getData);
    }
}
