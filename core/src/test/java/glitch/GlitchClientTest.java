package glitch;

import glitch.auth.GlitchScope;
import glitch.auth.UserCredential;
import glitch.auth.store.CachedStorage;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.UUID;
import org.junit.Test;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertTrue;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class GlitchClientTest {
    public static final GlitchClient CLIENT = GlitchClient.builder()
            .setClientId("fr681lv9fptltgo5r35nvqgczmgxap")
            .setClientSecret("olu3ppmijinmtr7m4ncfr8fy8h21ld")
            .setStorage(new CachedStorage(new LinkedHashSet<>()))
            .build();

    private final UserCredential credential = new UserCredential(
            "2xb5jjejkto6man5ufii7wliwrjzjw",
            "vmm5quqt1d4reo72e9vykkweemyc6ol4y4q5acb37h64ntt8ne"
    );

    @Test
    public void testValidation() {
        StepVerifier.create(CLIENT.getCredentialManager().valid(credential))
                .expectSubscription()
                .expectNextMatches(validate ->
                        validate.getUserId() == 120074641L &&
                                Objects.equals(validate.getLogin(), "sandalphon_ai") &&
                                validate.getScopes().contains(GlitchScope.CHAT_READ)
                ).expectComplete()
                .verify();
    }

    @Test
    public void buildCredential() {
        StepVerifier.create(CLIENT.getCredentialManager().buildFromCredentials(credential))
                .expectSubscription()
                .expectNextMatches(c ->
                        // checks if saved after subscription
                        CLIENT.getCredentialManager().getCredentialStorage().getById(c.getUserId())
                                .blockOptional().isPresent() &&
                                // asserting
                                c.getUserId() == 120074641L &&
                                Objects.equals(c.getLogin(), "sandalphon_ai") &&
                                c.getScopes().contains(GlitchScope.CHAT_READ) &&
                                Objects.equals(c.getAccessToken(), credential.getAccessToken()) &&
                                Objects.equals(c.getRefreshToken(), credential.getRefreshToken())
                ).expectComplete()
                .verify();
    }

    public void buildAuthUrl() {
        UUID state = UUID.randomUUID();

        String authUrl = CLIENT.getCredentialManager().buildAuthorizationUrl().addScope(GlitchScope.CHAT_READ, GlitchScope.CHAT_EDIT)
                .withForceVerify()
                .withState(state.toString())
                .withRedirectUri("https://localhost/twitch")
                .build();

        assertTrue(authUrl.contains("scope=chat:read+chat:edit"));
        assertTrue(authUrl.contains("redirect_uri=https://localhost/twitch"));
        assertTrue(authUrl.contains("force_verify=true"));
    }
}