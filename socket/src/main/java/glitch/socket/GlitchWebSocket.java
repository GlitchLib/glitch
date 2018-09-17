package glitch.socket;

import glitch.socket.events.Event;
import io.reactivex.subjects.PublishSubject;

public interface GlitchWebSocket {
    PublishSubject<Event> getDispatcher();

    void connect();

    void disconnect();

    void reconnect();
}
