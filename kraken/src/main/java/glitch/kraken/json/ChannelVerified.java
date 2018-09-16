package glitch.kraken.json;

import glitch.core.utils.Immutable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface ChannelVerified extends Channel {
    String getBroadcastType();
    String getStreamKey();
    String getEmail();
}
