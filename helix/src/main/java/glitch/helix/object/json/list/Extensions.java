package glitch.helix.object.json.list;

import com.google.common.collect.ImmutableList;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.helix.object.json.Extension;
import lombok.Data;

@Data
public class Extensions implements OrdinalList<Extension> {
    private final ImmutableList<Extension> data;
}
