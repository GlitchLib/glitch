package glitch.ws.event;

import glitch.ws.WebSocketClient;
import java.time.Instant;

public class ThrowableEvent<T extends Throwable, S extends WebSocketClient> extends ConnectionEvent<S> {

    private final T throwable;

    public ThrowableEvent(T throwable, S client, Instant createdAt, String eventId) {
        super(client, createdAt, eventId);
        this.throwable = throwable;
    }

    public T getThrowable() {
        return throwable;
    }

    public void printStackTrace() {
        throwable.printStackTrace();
    }

    public String getMessage() {
        return throwable.getMessage();
    }

    public StackTraceElement[] getStackTrace() {
        return throwable.getStackTrace();
    }

    public Throwable getCause() {
        return throwable.getCause();
    }

    public synchronized Throwable fillInStackTrace() {
        return throwable.fillInStackTrace();
    }
}
