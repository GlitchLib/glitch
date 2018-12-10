package glitch.helix.object.json.list;

import com.google.common.collect.ImmutableList;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.helix.object.json.ClipCreator;
import lombok.Data;

@Data
public class SingletonClipCreator implements OrdinalList<ClipCreator> {
    private final ImmutableList<ClipCreator> data;
}
