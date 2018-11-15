package glitch.api.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marking some methods or endpoint as Unofficial.
 * They are not documented yet into <a href="https://dev.twitch.tv/">Twitch Developer Site</a>.
 * They will be strictly documented, so you will using it into your own purpose.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface Unofficial {
    /**
     * Source of endpoints. If it is empty. Explanation is documented by {@link Unofficial this annotation}
     * @return Explanation or source of unofficial endpoint.
     */
    String value() default "";
}
