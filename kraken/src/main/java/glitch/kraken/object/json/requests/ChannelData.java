package glitch.kraken.object.json.requests;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import glitch.kraken.object.adapters.GameNameSerializer;
import glitch.kraken.object.json.Game;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChannelData {
    @SerializedName("status")
    private String title;
    @JsonAdapter(GameNameSerializer.class)
    private Game game;
    private Integer delay;
    @SerializedName("channel_feed_enabled")
    private Boolean channelFeedEnabled;
}
