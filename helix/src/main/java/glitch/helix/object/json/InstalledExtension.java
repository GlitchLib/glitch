package glitch.helix.object.json;

import lombok.Data;

@Data
public class InstalledExtension {
    private final String id;
    private final String version;
    private final String name;
    private final boolean active;
}
