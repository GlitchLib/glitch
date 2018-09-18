package glitch.core.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import glitch.auth.Scope;
import glitch.core.api.json.converters.ColorAdapter;
import glitch.core.api.json.converters.RxAdapter;
import glitch.core.api.json.converters.ScopeAdapter;
import glitch.core.api.json.converters.UserTypeAdapter;
import glitch.core.api.json.enums.UserType;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.awt.Color;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.ServiceLoader;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlitchUtils {
    public static final String BASE = "https://api.twitch.tv";
    public static final String KRAKEN = BASE + "/kraken";
    public static final String HELIX = BASE + "/helix";
    public static final String API = BASE + "/api";

    public static Gson createGson(@Nullable Map<Type, Object> adapters, boolean defaults) {
        GsonBuilder builder = new GsonBuilder()
                .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .serializeNulls();

        if (adapters == null) adapters = new LinkedHashMap<>();

        if (defaults) {
            adapters.put(Scope.class, new ScopeAdapter());
            adapters.put(Color.class, new ColorAdapter());
            adapters.put(UserType.class, new UserTypeAdapter());
//            adapters.put(Completable.class, new RxAdapter<>());
//            adapters.put(Flowable.class, new RxAdapter<>());
//            adapters.put(Maybe.class, new RxAdapter<>());
//            adapters.put(Observable.class, new RxAdapter<>());
//            adapters.put(Single.class, new RxAdapter<>());
        }

        for (Map.Entry<Type, Object> entry : adapters.entrySet()) {
            builder.registerTypeAdapter(entry.getKey(), entry.getValue());
        }

        for (TypeAdapterFactory factory : ServiceLoader.load(TypeAdapterFactory.class)) {
            builder.registerTypeAdapterFactory(factory);
        }

        return builder.create();
    }
}
