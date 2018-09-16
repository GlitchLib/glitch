package glitch.socket.events.actions;

import glitch.socket.GlitchWebSocket;
import glitch.socket.events.Event;
import glitch.socket.utils.EventImmutable;
import org.immutables.value.Value;

@EventImmutable
@Value.Immutable
public interface ThrowableEvent<T extends Throwable, S extends GlitchWebSocket> extends Event<S> {
    T getThrowable();

    @Value.Lazy
    default void printStackTrace() {
        getThrowable().printStackTrace();
    }

    @Value.Lazy
    default String getMessage() {
        return getThrowable().getMessage();
    }

    @Value.Lazy
    default StackTraceElement[] getStackTrace() {
        return getThrowable().getStackTrace();
    }

    @Value.Lazy
    default Throwable getCause() {
        return getThrowable().getCause();
    }

    @Value.Lazy
    default Throwable fillInStackTrace() {
        return getThrowable().fillInStackTrace();
    }
}
