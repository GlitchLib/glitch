package glitch.common.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import glitch.Config;
import glitch.auth.Scope;
import glitch.common.api.HttpClient;
import glitch.common.json.converters.ColorDeserializer;
import glitch.common.json.converters.ScopeDeserializer;
import java.awt.Color;

public class HttpUtils {
    private static HttpClient.Builder getBuilder() {
        return HttpClient.builder()
                .deserializer(Scope.class, new ScopeDeserializer())
                .deserializer(Color.class, new ColorDeserializer())
                .buildMapper(mapper -> mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        .enable(JsonGenerator.Feature.IGNORE_UNKNOWN)
                        .registerModule(new ParameterNamesModule())
                        .registerModule(new Jdk8Module())
                        .registerModule(new JavaTimeModule()));
    }

    public static HttpClient createForCredentials() {
        return getBuilder().build();
    }

    public static HttpClient createForApi(Config config, boolean kraken) {
        HttpClient.Builder builder = getBuilder().header("Client-ID", config.getClientId())
                .header("User-Agent", config.getUserAgent());

        if (kraken) {
            builder.header("Accept", "application/vnd.twitchtv.v5+json");
        }

        return builder.build();
    }
}
