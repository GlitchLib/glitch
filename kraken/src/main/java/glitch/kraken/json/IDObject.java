package glitch.kraken.json;

import com.fasterxml.jackson.annotation.JsonAlias;
import glitch.common.utils.Immutable;
import java.io.Serializable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface IDObject<T extends Serializable> {
    @JsonAlias({
            "id",
            "_id"
    })
    T getId();
}
