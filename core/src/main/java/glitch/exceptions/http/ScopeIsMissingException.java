package glitch.exceptions.http;

import glitch.auth.Scope;
import glitch.exceptions.GlitchException;
import lombok.Getter;

@Getter
public class ScopeIsMissingException extends GlitchException {
    private final Scope requiredScope;

    public ScopeIsMissingException(Scope requiredScope) {
        this.requiredScope = requiredScope;
    }

    @Override
    public String getMessage() {
        return "Scope is required: " + requiredScope.getValue();
    }
}
