package glitch.core.utils;

import org.immutables.value.Value;

@Value.Style(
        get = {"get*", "has*", "*"},
        builderVisibility = Value.Style.BuilderVisibility.SAME,
        visibility = Value.Style.ImplementationVisibility.PUBLIC,
        typeImmutable = "*Impl",
        defaultAsDefault = true,
        deepImmutablesDetection = true,
        allParameters = true,
        defaults = @Value.Immutable
)
public @interface Immutable {
}
