package glitch.kraken.services;

import glitch.api.http.Routes;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.Ingest;
import glitch.kraken.object.json.collections.Ingests;
import glitch.service.AbstractHttpService;
import reactor.core.publisher.Flux;

public class IngestService extends AbstractHttpService {
    public IngestService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Flux<Ingest> getIngestServerList() {
        return exchangeTo(Routes.get("/ingests").newRequest(), Ingests.class)
                .flatMapIterable(OrdinalList::getData);
    }
}
