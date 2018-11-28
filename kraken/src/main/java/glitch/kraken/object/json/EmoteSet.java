package glitch.kraken.object.json;

import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;

import java.util.List;

@Data
public class EmoteSet implements IDObject<Integer> {
    private final Integer id;
    private final List<EmoteSetData> emotes;

    public EmoteSetData getByName(String name) {
        return emotes.stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
    }

    public EmoteSetData getById(Long id) {
        return emotes.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }
}
