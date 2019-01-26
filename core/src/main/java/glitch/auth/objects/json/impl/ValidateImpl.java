package glitch.auth.objects.json.impl;

import glitch.auth.GlitchScope;
import glitch.auth.objects.json.Validate;

import java.util.Objects;
import java.util.Set;

public final class ValidateImpl implements Validate {
    private final String clientId;
    private final String login;
    private final Set<GlitchScope> scopes;
    private final Long userId;

    public ValidateImpl(String clientId, String login, Set<GlitchScope> scopes, Long userId) {
        this.clientId = clientId;
        this.login = login;
        this.scopes = scopes;
        this.userId = userId;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public Set<GlitchScope> getScopes() {
        return scopes;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidateImpl)) return false;
        ValidateImpl validate = (ValidateImpl) o;
        return getClientId().equals(validate.getClientId()) &&
                getLogin().equals(validate.getLogin()) &&
                getScopes().equals(validate.getScopes()) &&
                getUserId().equals(validate.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClientId(), getLogin(), getScopes(), getUserId());
    }

    @Override
    public String toString() {
        return "Validate{" +
                "clientId='" + clientId + '\'' +
                ", login='" + login + '\'' +
                ", scopes=" + scopes +
                ", userId=" + userId +
                '}';
    }
}
