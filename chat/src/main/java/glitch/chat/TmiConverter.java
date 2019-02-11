package glitch.chat;

import glitch.api.ws.IEventConverter;
import glitch.api.ws.events.IEvent;
import glitch.chat.events.IRCEvent;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class TmiConverter implements IEventConverter<GlitchChat> {

    private TmiConverter() {
    }

    static TmiConverter create() {
        return new TmiConverter();
    }

    @Override
    public IEvent<GlitchChat> convert(GlitchChat client, String raw) {
        if (raw.contains(System.lineSeparator())) {
            for (String r : raw.split(System.lineSeparator())) {
                return convert(client, r);
            }
        }

        return new IRCEvent(client, raw);

    }
}
