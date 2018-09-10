package glitch.core.api.json;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import glitch.core.utils.Immutable;
import java.io.Serializable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@JsonDeserialize(builder = IDObjectBuilder.class)
public interface IDObject<T extends Serializable> {
    @JsonAlias({
            "id",
            "_id"
    })
    T getId();
}
