package glitch.kraken.object.json.list;

import com.google.common.collect.ImmutableList;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.Team;
import lombok.Data;

@Data
public class Teams implements OrdinalList<Team> {
    private final ImmutableList<Team> data;
}
