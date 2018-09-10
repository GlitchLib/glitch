package glitch.kraken.json;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import glitch.core.api.json.IDObject;
import java.awt.Color;
import java.util.Locale;
import javax.annotation.Nullable;

public interface Channel extends IDObject<Long> {
    @Nullable
    Locale getBroadcasterLanguage();

    String getDisplayName();

    Long getFollowers();

    String getGame();

    Locale getLanguage();

    String getLogo();

    boolean isMature();

    @JsonAlias({"username", "name"})
    String getUsername();

    boolean isPartner();

    @Nullable
    String getProfileBanner();

    @Nullable
    Color getProfileBannerBackgroundColor();

    @JsonProperty("status")
    String getTitle();

    String getUrl();

    @Nullable
    String getVideoBanner();

    Long getViews();
}
