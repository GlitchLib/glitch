package glitch.core.api.json.expanders;

import feign.Param;
import glitch.auth.Credential;

public class CredentialTokenExpander implements Param.Expander {
    @Override
    public String expand(Object value) {
        if (value instanceof Credential) {
            return ((Credential) value).getAccessToken();
        } else return value.toString();
    }
}
