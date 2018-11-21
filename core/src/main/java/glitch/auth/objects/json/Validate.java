package glitch.auth.objects.json;

import glitch.auth.Scope;

import java.util.Set;

public interface Validate {
    String getClientId();

    String getLogin();

    Set<Scope> getScopes();

    Long getUserId();
}
