package glitch.kraken.object.json.requests;

import com.google.common.collect.ImmutableSet;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import glitch.kraken.object.adapters.GameNameSerializer;
import glitch.kraken.object.json.Game;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Locale;

@Data
@Builder(builderClassName = "Builder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoBody {
    private final String description;
    @JsonAdapter(GameNameSerializer.class)
    private final Game game;
    private final Locale language;
    @SerializedName("tag_list")
    private final ImmutableSet<String> tags;
    private final String title;
}
