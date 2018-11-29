package glitch.kraken.object.json.list;

import com.google.common.collect.ImmutableList;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.FeatureStream;
import lombok.Data;

@Data
public class FeatureStreams implements OrdinalList<FeatureStream> {
    private final ImmutableList<FeatureStream> data;
}
