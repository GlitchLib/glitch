package glitch.kraken.object.json;

import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;

@Data
public class Game implements IDObject<Long> {
    private final Long id;
    private final Image box;
    private final Long gigantbombId;
    private final Image logo;
    private final String name;
    private final Long popularity;
}
