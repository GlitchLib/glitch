package glitch.common.utils;

import org.immutables.value.Value;

@Value.Style(
        get = {"get*", "has*", "*"},
        builderVisibility = Value.Style.BuilderVisibility.PUBLIC,
        visibility = Value.Style.ImplementationVisibility.PRIVATE,
        typeImmutable = "*Impl",
        defaultAsDefault = true,
        deepImmutablesDetection = true,
        defaults = @Value.Immutable(copy = false)
)
public @interface Immutable {
}
