package glitch.kraken.object.json.list;

import com.google.common.collect.ImmutableList;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.User;
import lombok.Data;

@Data
public class Editors implements OrdinalList<User> {
    private final ImmutableList<User> data;
}
