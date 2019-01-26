package glitch.auth.objects.json;

import glitch.auth.GlitchScope;

import java.util.Set;

public interface Validate {
    String getClientId();

    String getLogin();

    Set<GlitchScope> getScopes();

    Long getUserId();
}
