package glitch.auth;

import glitch.auth.json.AccessToken;
import glitch.auth.json.Validate;
import glitch.core.utils.Immutable;
import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
@Gson.TypeAdapters(fieldNamingStrategy = true)
public interface Credential extends AccessToken, Validate {}
