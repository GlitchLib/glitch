package glitch.exceptions.http;

import glitch.auth.GlitchScope;

public class ScopeIsMissingException extends RequestException {
    private final GlitchScope requiredScope;

    public ScopeIsMissingException(GlitchScope requiredScope) {
        super("Scope is required: " + requiredScope.getValue());
        this.requiredScope = requiredScope;
    }

    public GlitchScope getRequiredScope() {
        return requiredScope;
    }
}
