package glitch.socket;

import glitch.GlitchClient;
import glitch.socket.events.Event;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public interface GlitchWebSocket {
    GlitchClient getClient();

    PublishSubject<Event> getDispatcher();

    Single<Void> connect();

    Single<Void> disconnect();

    Single<Void> reconnect();

    default void connectAsync() {
        connect().blockingGet();
    }

    default void disconnectAsync() {
        disconnect().blockingGet();
    }

    default void reconnectAsync() {
        reconnect().blockingGet();
    }

    <S extends GlitchWebSocket, E extends Event<S>> Observable<E> listenOn(Class<E> eventType);
}
