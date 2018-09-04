package glitch.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class CredentialCreator {
    private String accessToken;
    private String refreshToken;
}
