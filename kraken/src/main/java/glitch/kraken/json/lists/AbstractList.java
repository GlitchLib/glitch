package glitch.kraken.json.lists;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.List;

public interface AbstractList<T> {
    @JsonAlias({
            "data",
            "actions"
    })
    List<T> getData();
}
