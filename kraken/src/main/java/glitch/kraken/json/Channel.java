package glitch.kraken.json;

import com.google.gson.annotations.SerializedName;
import glitch.core.api.json.IDObject;
import glitch.core.utils.Immutable;
import java.awt.Color;
import java.util.Locale;
import javax.annotation.Nullable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface Channel extends IDObject<Long> {
    @Nullable
    Locale getBroadcasterLanguage();

    String getDisplayName();

    Long getFollowers();

    String getGame();

    Locale getLanguage();

    String getLogo();

    boolean isMature();

    @SerializedName(value = "name", alternate = {"username", "login"})
    String getUsername();

    boolean isPartner();

    @Nullable
    String getProfileBanner();

    @Nullable
    Color getProfileBannerBackgroundColor();

    @SerializedName("status")
    String getTitle();

    String getUrl();

    @Nullable
    String getVideoBanner();

    Long getViews();
}
