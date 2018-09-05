package glitch;

import glitch.chat.BotConfig;
import glitch.utils.Immutable;
import org.immutables.value.Value;

@Immutable
@Value.Immutable
public interface Config {
    String getClientId();
    String getClientSecret();
    BotConfig getBotCredentials();

    default String getUserAgent() {
        return String.format("Glitch [v%s] [Rev:%s#%s]", Property.VERSION, Property.COMMIT_ID, Property.REVISION);
    }


}
