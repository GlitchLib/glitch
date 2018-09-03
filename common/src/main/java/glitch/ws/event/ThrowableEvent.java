package glitch.ws.event;

import glitch.utils.Immutable;
import glitch.ws.WebSocketClient;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface ThrowableEvent<T extends Throwable, S extends WebSocketClient> extends SocketEvent<S> {
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
