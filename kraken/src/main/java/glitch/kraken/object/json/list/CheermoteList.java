package glitch.kraken.object.json.list;

import com.google.common.collect.ImmutableList;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.Cheermote;
import lombok.Data;

@Data
public class CheermoteList implements OrdinalList<Cheermote> {
    private final ImmutableList<Cheermote> data;

    @Override
    public int size() {
        return data.size();
    }
}
