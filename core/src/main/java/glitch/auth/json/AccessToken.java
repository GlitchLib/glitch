package glitch.auth.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import glitch.auth.Scope;
import glitch.auth.json.converters.ExpireInstantDeserializer;
import glitch.core.utils.Immutable;
import java.time.Instant;
import java.util.Set;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@JsonDeserialize(builder = AccessTokenBuilder.class)
public interface AccessToken {
    String getAccessToken();

    String getRefreshToken();

    @JsonDeserialize(using = ExpireInstantDeserializer.class)
    Instant expiresIn();

    @JsonProperty("scope")
    Set<Scope> getScopes();
}
