package glitch.exceptions;

public class ServiceNotFoundException extends GlitchException {
    public ServiceNotFoundException(Class<?> service) {
        super(String.format("Service \"%s\" is not registered", service.getSimpleName()));
    }
}
