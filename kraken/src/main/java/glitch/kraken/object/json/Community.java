package glitch.kraken.object.json;

import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;

import java.util.Locale;
import java.util.UUID;

@Data
public class Community implements IDObject<UUID> {
    private final UUID id;
    private final Long ownerId;
    private final String name;
    private final String displayName;
    private final String avatarImageUrl;
    private final String coverImageUrl;
    private final String description;
    private final String descriptionHtml;
    private final String rules;
    private final String rulesHtml;
    private final Locale language;
    private final String summary;
}
