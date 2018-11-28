package glitch.kraken.object.json.list;

import com.google.common.collect.Multimap;
import glitch.kraken.object.json.EmoteSet;
import glitch.kraken.object.json.EmoteSetData;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;

@Data
public class EmoteSets {
    @Getter(AccessLevel.NONE)
    private final Multimap<Integer, EmoteSetData> emoticonSets;

    public EmoteSet get(Integer id) {
        if (emoticonSets.containsKey(id)) {
            return new EmoteSet(id, new ArrayList<>(emoticonSets.get(id)));
        }

        return null;
    }
}
