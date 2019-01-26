package glitch.auth.objects.json;

import com.google.gson.annotations.SerializedName;
import glitch.auth.GlitchScope;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Set;

public interface AccessToken {
    String getAccessToken();

    String getRefreshToken();

    @Nullable
    @SerializedName("expires_in")
    Instant getExpiredAt();

    @SerializedName("scope")
    Set<GlitchScope> getScopes();
}
