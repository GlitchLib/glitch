package io.glitchlib.v5.service

import io.glitchlib.GlitchClient
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.model.OrdinalList
import io.glitchlib.v5.internal.AbstractKrakenService
import io.glitchlib.v5.model.json.Ingest

class IngestService(client: GlitchClient) : AbstractKrakenService(client) {
    val ingestServerList
        get() = get<OrdinalList<Ingest>>("/ingests").bodyFlowable
}
