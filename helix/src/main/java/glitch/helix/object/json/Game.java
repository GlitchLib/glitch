package glitch.helix.object.json;

import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;

@Data
public class Game implements IDObject<Long> {
    private final Long id;
    private final String name;
    private final String boxArtUrl;
}
