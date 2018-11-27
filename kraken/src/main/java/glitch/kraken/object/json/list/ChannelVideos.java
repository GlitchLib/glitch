package glitch.kraken.object.json.list;

import com.google.common.collect.ImmutableList;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.object.json.Video;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class ChannelVideos implements OrdinalList<Video> {
    private final ImmutableList<Video> data;
    @Getter(AccessLevel.NONE)
    private final int total;

    @Override
    public int size() {
        return total;
    }
}
