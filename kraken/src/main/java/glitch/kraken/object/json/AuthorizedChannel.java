package glitch.kraken.object.json;

import glitch.kraken.object.enums.BroadcasterType;
import glitch.kraken.object.json.interfaces.IAuthorizedUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.time.Instant;
import java.util.Locale;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthorizedChannel extends Channel implements IAuthorizedUser {
    private final String email;
    private final String streamKey;

    public AuthorizedChannel(Long id, String username, String displayName, Instant createdAt, Instant updatedAt, String logo, String userBio, boolean mature, String title, Locale broadcasterLanguage, String broadcastSoftware, String game, Locale language, boolean partner, String videoBanner, String profileBanner, Color profileBannerBackgroundColor, String url, long views, long followers, BroadcasterType broadcasterType, boolean privateVideo, boolean privacyOptionsEnabled, String email, String streamKey) {
        super(id, username, displayName, createdAt, updatedAt, logo, userBio, mature, title, broadcasterLanguage, broadcastSoftware, game, language, partner, videoBanner, profileBanner, profileBannerBackgroundColor, url, views, followers, broadcasterType, privateVideo, privacyOptionsEnabled);
        this.email = email;
        this.streamKey = streamKey;
    }
}
