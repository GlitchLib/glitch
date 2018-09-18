package glitch.core.utils.http.instances;

import feign.Target;
import glitch.core.utils.GlitchUtils;

public class ApiInstance<T> extends Target.HardCodedTarget<T> {
    public ApiInstance(Class<T> type) {
        super(type, GlitchUtils.API);
    }
}
