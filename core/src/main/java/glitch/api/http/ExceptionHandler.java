package glitch.api.http;

/**
 * Handling your own exception into loggers or own Alerts
 */
@FunctionalInterface
public interface ExceptionHandler {
    /**
     * Handling Exception
     * @param throwable throwable exception.
     */
    void handle(Throwable throwable);
}
