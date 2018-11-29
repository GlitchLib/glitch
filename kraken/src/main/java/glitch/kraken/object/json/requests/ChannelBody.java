package glitch.kraken.object.json.requests;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import glitch.kraken.object.adapters.GameNameSerializer;
import glitch.kraken.object.json.Game;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChannelBody {
    @SerializedName("status")
    private final String title;
    @JsonAdapter(GameNameSerializer.class)
    private final Game game;
    private final Integer delay;
    @SerializedName("channel_feed_enabled")
    private final Boolean channelFeedEnabled;
}
