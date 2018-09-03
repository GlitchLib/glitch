package glitch.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface Unofficial {
    /**
     * Source of endpoints
     * @return Explanation or source of unofficial endpoint
     */
    String value() default "";
}
