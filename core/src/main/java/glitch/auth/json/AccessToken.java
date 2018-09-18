package glitch.auth.json;

import com.google.gson.annotations.SerializedName;
import glitch.auth.Scope;
import glitch.core.utils.Immutable;
import java.util.Calendar;
import java.util.Set;
import javax.annotation.Nullable;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface AccessToken {
    String getAccessToken();

    String getRefreshToken();

    @Nullable
    @SerializedName("expires_in")
    Calendar expiredAt();

    @SerializedName("scope")
    Set<Scope> getScopes();
}
