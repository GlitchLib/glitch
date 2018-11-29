package glitch.kraken.object.json.list;

import com.google.common.collect.ImmutableList;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.Ingest;
import lombok.Data;

@Data
public class Ingests implements OrdinalList<Ingest> {
    private final ImmutableList<Ingest> data;
}
