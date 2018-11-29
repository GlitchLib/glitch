package glitch.kraken.object.json;

import com.google.common.collect.ImmutableList;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class TeamUsers extends Team {
    private final ImmutableList<User> users;

    public TeamUsers(Long id, String background, String banner, Instant createdAt, Instant updatedAt, String displayName, String description, String logo, String name, ImmutableList<User> users) {
        super(id, background, banner, createdAt, updatedAt, displayName, description, logo, name);
        this.users = users;
    }
}
