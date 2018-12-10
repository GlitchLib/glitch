package glitch.helix.object.json.list;

import com.google.common.collect.ImmutableList;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.helix.object.json.InstalledExtension;
import lombok.Data;

@Data
public class InstalledExtensions implements OrdinalList<InstalledExtension> {
    private final ImmutableList<InstalledExtension> data;
}
