package glitch.auth.objects.impl;

import glitch.auth.Scope;
import glitch.auth.objects.Validate;
import lombok.Data;

import java.util.Set;

@Data
public class ValidateImpl implements Validate {
    private final String clientId;

    private final String login;

    private final Set<Scope> scopes;

    private final Long userId;
}
