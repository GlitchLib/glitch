package glitch.core.utils;

import com.google.common.collect.Multimap;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import glitch.auth.Scope;
import glitch.core.api.json.converters.ColorDeserializer;
import glitch.core.api.json.converters.ScopeDeserializer;
import glitch.core.api.json.converters.UserTypeSerialization;
import glitch.core.api.json.enums.UserType;
import glitch.core.utils.http.CredentialConverterFactory;
import java.awt.Color;
import java.io.IOException;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlitchUtils {
    public static final String BASE = "https://api.twitch.tv";
    public static final String KRAKEN = BASE + "/kraken";
    public static final String HELIX = BASE + "/helix";

    public static <X> Retrofit createHttpClient(
            String baseUrl,
            final Multimap<String, String> defaultHeaders,
            Map<Class<X>, JsonSerializer<X>> serializers,
            Map<Class<X>, JsonDeserializer<X>> deserializers
    ) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(new CredentialConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .client(new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final Request.Builder request = chain.request().newBuilder();

                        for (Map.Entry<String, String> entry : defaultHeaders.entries()) {
                            request.addHeader(entry.getKey(), entry.getValue());
                        }

                        return chain.proceed(request.build());
                    }
                }).build())
                .addConverterFactory(GsonConverterFactory.create(createGson(serializers, deserializers)))
                .build();
    }

    public static <X> Gson createGson(
            Map<Class<X>, JsonSerializer<X>> serializers,
            Map<Class<X>, JsonDeserializer<X>> deserializers
    ) {
        GsonBuilder builder = new GsonBuilder()
                .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .serializeNulls();

        for (Map.Entry<Class<X>, JsonSerializer<X>> entry : serializers.entrySet()) {
            builder.registerTypeAdapter(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Class<X>, JsonDeserializer<X>> entry : deserializers.entrySet()) {
            builder.registerTypeAdapter(entry.getKey(), entry.getValue());
        }

        return builder.create();
    }


    @SuppressWarnings("unchecked")
    public static <X> void registerDeserializers(Map<Class<X>, JsonDeserializer<X>> deserializers) {
        deserializers.put((Class<X>) Scope.class, (JsonDeserializer<X>) new ScopeDeserializer());
        deserializers.put((Class<X>) Color.class, (JsonDeserializer<X>) new ColorDeserializer());
        deserializers.put((Class<X>) UserType.class, (JsonDeserializer<X>) new UserTypeSerialization());
    }

    @SuppressWarnings("unchecked")
    public static <X> void registerSerializers(Map<Class<X>, JsonSerializer<X>> serializers) {
        serializers.put((Class<X>) Scope.class, (JsonSerializer<X>) new ScopeDeserializer());
        serializers.put((Class<X>) Color.class, (JsonSerializer<X>) new ColorDeserializer());
        serializers.put((Class<X>) UserType.class, (JsonSerializer<X>) new UserTypeSerialization());
    }
}
