package glitch.helix.object.json;

import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;

@Data
public class ClipCreator implements IDObject<String> {
    private final String id;
    private final String editUrl;
}
