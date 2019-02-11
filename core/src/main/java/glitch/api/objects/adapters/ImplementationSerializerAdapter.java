package glitch.api.objects.adapters;

import com.google.gson.*;
import java.lang.reflect.Type;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class ImplementationSerializerAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
    @Override
    public T deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        Class<T> realType = getTypedArgument(type);
        if (realType.isInterface() && realType.isAnnotationPresent(SerializeTo.class)) {
            Class<?> serializedClass = realType.getAnnotation(SerializeTo.class).value();

            if (serializedClass.isAssignableFrom(realType)) {
                return context.deserialize(json, serializedClass);
            }

        }

        throw new JsonParseException("Cannot deserialize interfaced implementation of " + realType.getSimpleName());
    }

    @Override
    public JsonElement serialize(T src, Type type, JsonSerializationContext context) {
        return context.serialize(src, type);
    }

    @SuppressWarnings("unchecked")
    private Class<T> getTypedArgument(Type type) {
        return (Class<T>) type;
    }
}
