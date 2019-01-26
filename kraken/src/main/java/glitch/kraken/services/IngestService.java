package glitch.kraken.services;

import glitch.service.AbstractHttpService;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.Ingest;
import glitch.kraken.object.json.list.Ingests;
import reactor.core.publisher.Flux;

public class IngestService extends AbstractHttpService {
    public IngestService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Flux<Ingest> getIngestServerList() {
        return exchange(get("/ingests", Ingests.class)).toFlux(OrdinalList::getData);
    }
}
