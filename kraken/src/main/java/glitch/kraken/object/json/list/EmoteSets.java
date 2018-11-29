package glitch.kraken.object.json.list;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import glitch.kraken.object.json.Emote;
import glitch.kraken.object.json.EmoteSet;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Data
public class EmoteSets {
    @Getter(AccessLevel.NONE)
    private final Multimap<Integer, Emote> emoticonSets;

    public ImmutableSet<EmoteSet> toEmoteSets() {
        return ImmutableSet.copyOf(emoticonSets.asMap().entrySet().stream().map(e -> new EmoteSet(e.getKey(), new ArrayList<>(e.getValue()))).collect(Collectors.toSet()));
    }

    public EmoteSet get(Integer id) {
        return toEmoteSets().stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }
}
