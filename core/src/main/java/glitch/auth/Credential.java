package glitch.auth;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import glitch.auth.json.AccessToken;
import glitch.auth.json.Validate;
import glitch.common.utils.Immutable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@JsonDeserialize(builder = CredentialBuilder.class)
public interface Credential extends AccessToken, Validate {

    static CredentialCreator from(String accessToken, String refreshToken) {
        return new CredentialCreator(accessToken, refreshToken);
    }
}
