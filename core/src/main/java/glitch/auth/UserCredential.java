package glitch.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCredential {
    private final String accessToken;
    private final String refreshToken;
}
