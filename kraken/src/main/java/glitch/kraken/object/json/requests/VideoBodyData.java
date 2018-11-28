package glitch.kraken.object.json.requests;

import com.google.gson.annotations.SerializedName;
import glitch.kraken.object.json.Game;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Accessors(fluent = true)
public class VideoBodyData {
    private String description;
    private Game game;
    private Locale language;
    @SerializedName("tag_list")
    private final Set<String> tags = new LinkedHashSet<>();
    private String title;

    public void tags(String... tags) {
        tags(Arrays.asList(tags));
    }

    public void tags(Collection<String> tags) {
        this.tags.addAll(tags.stream().filter(s -> s.length() <= 100).collect(Collectors.toList()));
    }
}
