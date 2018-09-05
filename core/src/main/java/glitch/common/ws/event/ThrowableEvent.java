package glitch.common.ws.event;

import glitch.common.utils.Immutable;
import glitch.common.ws.WebSocketClient;
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
