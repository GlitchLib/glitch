package glitch.helix.object.json;

import com.google.common.collect.ImmutableList;
import glitch.helix.object.enums.ExtensionType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class Extension {
    private final String id;
    private final String version;
    private final String name;
    @Accessors(fluent = true)
    private final boolean canActivate;
    private final ImmutableList<ExtensionType> type;
}
