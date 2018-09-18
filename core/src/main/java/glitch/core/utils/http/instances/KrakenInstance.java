package glitch.core.utils.http.instances;

import feign.Target;
import glitch.core.utils.GlitchUtils;

public class KrakenInstance<T> extends Target.HardCodedTarget<T> {
    public KrakenInstance(Class<T> type) {
        super(type, GlitchUtils.KRAKEN);
    }
}
