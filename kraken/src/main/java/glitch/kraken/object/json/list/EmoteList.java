package glitch.kraken.object.json.list;

import com.google.common.collect.ImmutableList;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.Emote;
import lombok.Data;

@Data
public class EmoteList implements OrdinalList<Emote> {
    private final ImmutableList<Emote> data;
}
