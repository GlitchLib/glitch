package glitch.core.utils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import io.reactivex.*;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

class RxTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        if (adapts(type)) {
            if (type.getRawType().isAssignableFrom(Completable.class)) {
                return (TypeAdapter<T>) new TypeAdapter<Completable>() {
                    @Override
                    public void write(final JsonWriter out, Completable value) throws IOException {
                        value.subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                out.value(true);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                out.value(false);
                            }
                        });
                    }

                    @Override
                    public Completable read(JsonReader in) throws IOException {
                        if (in.peek() == JsonToken.NULL || (in.peek() == JsonToken.BOOLEAN && in.nextBoolean())) {
                            return Completable.complete();
                        } else return Completable.never();
                    }
                };
            }
            else if (type.getRawType().isAssignableFrom(Flowable.class)) {
                return (TypeAdapter<T>) new TypeAdapter<Flowable<?>>() {
                    @Override
                    public void write(final JsonWriter out, Flowable<?> value) throws IOException {
                        value.subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                out.value(gson.toJson(o));
                            }
                        });
                    }

                    @Override
                    public Flowable<?> read(JsonReader in) throws IOException {
                        return Flowable.just(gson.fromJson(in, getTypedArgument(type)));
                    }
                };
            }
            else if (type.getRawType().isAssignableFrom(Maybe.class)) {
                return (TypeAdapter<T>) new TypeAdapter<Maybe<?>>() {
                    @Override
                    public void write(final JsonWriter out, Maybe<?> value) throws IOException {
                        value.subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                out.value(gson.toJson(o));
                            }
                        });
                    }

                    @Override
                    public Maybe<?> read(JsonReader in) throws IOException {
                        return Maybe.just(gson.fromJson(in, getTypedArgument(type)));
                    }
                };
            }
            else if (type.getRawType().isAssignableFrom(Observable.class)) {
                return (TypeAdapter<T>) new TypeAdapter<Observable<?>>() {
                    @Override
                    public void write(final JsonWriter out, Observable<?> value) throws IOException {
                        value.subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                out.value(gson.toJson(o));
                            }
                        });
                    }

                    @Override
                    public Observable<?> read(JsonReader in) throws IOException {
                        return Observable.just(gson.fromJson(in, getTypedArgument(type)));
                    }
                };
            }
            else if (type.getRawType().isAssignableFrom(Single.class)) {
                return (TypeAdapter<T>) new TypeAdapter<Single<?>>() {
                    @Override
                    public void write(final JsonWriter out, Single<?> value) throws IOException {
                        value.subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                out.value(gson.toJson(o));
                            }
                        });
                    }

                    @Override
                    public Single<?> read(JsonReader in) throws IOException {
                        return Single.just(gson.fromJson(in, getTypedArgument(type)));
                    }
                };
            } else return null;
        }
        return null;
    }

    <T> Class<?> getTypedArgument(TypeToken<T> type) {
        return (Class<?>) ((ParameterizedType) type.getRawType().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    boolean adapts(TypeToken<?> type) {
        return Arrays.asList(Flowable.class, Maybe.class, Observable.class, Single.class)
                .contains(type.getRawType());
    }
}
