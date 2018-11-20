package glitch.chat.object.irc;

import com.google.common.collect.ImmutableRangeSet;
import glitch.api.objects.json.interfaces.IDObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Emote implements IDObject<Integer> {
    private final Integer id;
    private final ImmutableRangeSet<Integer> indexRange;

    public String getEmoteUrl(Emote.Size emoteSize) {
        return String.format("http://static-cdn.jtvnw.net/emoticons/v1/%d/%s", getId(), emoteSize.value);
    }

    public enum Size {
        X1(1.0),
        X2(2.0),
        X3(3.0);

        private final double value;

        Size(double value) {
            this.value = value;
        }
    }
}
