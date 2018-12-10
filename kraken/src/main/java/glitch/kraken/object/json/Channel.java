package glitch.kraken.object.json;

import com.google.gson.annotations.SerializedName;
import glitch.api.objects.enums.BroadcasterType;
import glitch.kraken.object.json.interfaces.IUser;
import lombok.Data;

import java.awt.*;
import java.time.Instant;
import java.util.Locale;

@Data
public class Channel implements IUser {
    @SerializedName(value = "id", alternate = "_id")
    private final Long id;
    @SerializedName(value = "username", alternate = {"login", "name"})
    private final String username;
    private final String displayName;

    private final Instant createdAt;
    private final Instant updatedAt;

    private final String logo;

    @SerializedName(value = "bio", alternate = {"description"})
    private final String userBio;
    private final boolean mature;
    @SerializedName("status")
    private final String title;
    private final Locale broadcasterLanguage;
    private final String broadcastSoftware;
    private final String game;
    private final Locale language;
    private final boolean partner;
    private final String videoBanner;
    private final String profileBanner;
    private final Color profileBannerBackgroundColor;
    private final String url;
    private final long views;
    private final long followers;
    private final BroadcasterType broadcasterType;
    private final boolean privateVideo;
    private final boolean privacyOptionsEnabled;
}
