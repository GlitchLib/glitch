package glitch.socket.utils;

import org.immutables.value.Value;

@Value.Style(
        get = {"get*", "has*", "*"},
        visibility = Value.Style.ImplementationVisibility.PUBLIC,
        typeImmutable = "*Impl",
        defaultAsDefault = true,
        deepImmutablesDetection = true,
        allParameters = true,
        defaults = @Value.Immutable(copy = false)
)
public @interface EventImmutable {
}
