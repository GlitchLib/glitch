package glitch.kraken.object.json.list;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.User;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class UserList implements OrdinalList<User> {
    private final ImmutableList<User> data;

    @Accessors(fluent = true)
    @SerializedName("_total")
    private final int size;
}
