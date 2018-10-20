package glitch.core.api.json.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.reactivex.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class RxAdapter<T, R> implements JsonDeserializer<T> {

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(final JsonElement json, Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final Type typedParameter = getTypedClass((ParameterizedType) typeOfT);

        if (isCompletable(typeOfT) && typedParameter == null) {
            return (T) Completable.complete();
        } else {
            R response = context.deserialize(json, typedParameter);
            if (isFlowable(typeOfT)) {
                return (T) getFlowable(response);
            } else if (isMaybe(typeOfT)) {
                return (T) getMaybe(response);
            } else if (isObservable(typeOfT)) {
                return (T) getObservable(response);
            } else if (isSingle(typeOfT)) {
                return (T) getSingle(response);
            }
        }

        return (T) context.deserialize(json, typeOfT);
    }

    private Single<R> getSingle(R response) {
        return Single.just(response);
    }

    private Observable<R> getObservable(R response) {
        return Observable.just(response);
    }

    private Maybe getMaybe(R response) {
        return Maybe.just(response);
    }

    private Flowable<R> getFlowable(R response) {
        return Flowable.just(response);
    }

    private boolean isCompletable(Type type) {
        return type == Completable.class;
    }

    private boolean isFlowable(Type type) {
        return type == Flowable.class;
    }

    private boolean isMaybe(Type type) {
        return type == Maybe.class;
    }

    private boolean isObservable(Type type) {
        return type == Observable.class;
    }

    private boolean isSingle(Type type) {
        return type == Single.class;
    }

    private Type getTypedClass(ParameterizedType type) {
        return type.getActualTypeArguments()[0];
    }
}
