package glitch.socket;

import glitch.socket.events.Event;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public interface GlitchWebSocket {
    PublishSubject<Event> getDispatcher();

    void connect();

    void disconnect();

    void reconnect();

    <S extends GlitchWebSocket, E extends Event<S>> Observable<E> listenOn(Class<E> eventType);
}
