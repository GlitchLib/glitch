package glitch.events;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class EventManager {

    private final PublishSubject<Event> eventPublisher = PublishSubject.create();

    public void dispatch(Event event) {
        eventPublisher.onNext(event);
    }

    public <E extends Event> Observable<E> on(Class<E> eventClass) {
        return eventPublisher.ofType(eventClass);
    }
}
