package glitch.kraken.object.json.list;

import com.google.common.collect.ImmutableList;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.Video;
import lombok.Data;

@Data
public class Videos implements OrdinalList<Video> {
    private final ImmutableList<Video> data;
}
