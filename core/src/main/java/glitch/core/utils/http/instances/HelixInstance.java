package glitch.core.utils.http.instances;

import feign.Target;
import glitch.core.utils.GlitchUtils;

public class HelixInstance<T> extends Target.HardCodedTarget<T> {
    public HelixInstance(Class<T> type) {
        super(type, GlitchUtils.KRAKEN);
    }
}
