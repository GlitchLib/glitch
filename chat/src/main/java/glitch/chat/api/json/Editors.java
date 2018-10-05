package glitch.chat.api.json;

import com.google.common.collect.ImmutableList;
import glitch.auth.Credential;
import glitch.core.api.json.OrdinalList;
import java.util.AbstractList;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Editors extends AbstractList<User> implements OrdinalList<User> {
    private final ImmutableList<User> data;

    @Override
    public User get(int index) {
        return data.get(index);
    }

    @Override
    public int size() {
        return data.size();
    }

    public boolean isEditor(Long id) {
        return data.stream().anyMatch(user -> user.getId().equals(id));
    }

    public boolean isEditor(Credential credential) {
        return isEditor(credential.getUserId());
    }
}
