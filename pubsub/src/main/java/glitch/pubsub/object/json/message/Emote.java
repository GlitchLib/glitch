package glitch.pubsub.object.json.message;

import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;

@Data
public class Emote implements IDObject<Long> {
    private final Long id;
    private final Integer start;
    private final Integer end;

    public String getEmoteUrl(Size emoteSize) {
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
