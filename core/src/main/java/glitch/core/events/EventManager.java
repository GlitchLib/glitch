package glitch.core.events;

import glitch.GlitchClient;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventManager {
    private final GlitchClient client;

    private final PublishSubject<Event> eventPublisher = PublishSubject.create();

    public void dispatch(Event event) {
        eventPublisher.onNext(event);
    }

    public <E extends Event> Observable<E> on(Class<E> eventClass) {
        return eventPublisher.ofType(eventClass);
    }
}
