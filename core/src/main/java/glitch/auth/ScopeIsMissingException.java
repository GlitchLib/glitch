package glitch.auth;

import java.io.IOException;
import lombok.Getter;

@Getter
public class ScopeIsMissingException extends IOException {
    private final Scope requiredScope;

    public ScopeIsMissingException(Scope requiredScope) {
        super("Scope is required: " + requiredScope.getValue());
        this.requiredScope = requiredScope;
    }
}
