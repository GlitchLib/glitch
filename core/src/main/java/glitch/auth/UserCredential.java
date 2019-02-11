package glitch.auth;

public final class UserCredential {
    private final String accessToken;
    private final String refreshToken;

    public UserCredential(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
