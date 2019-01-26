package glitch.service;

import glitch.GlitchClient;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public interface IService {

    /**
     * Getting {@link GlitchClient}
     * @return the {@link GlitchClient Glitch Client}
     */
    GlitchClient getClient();
}
