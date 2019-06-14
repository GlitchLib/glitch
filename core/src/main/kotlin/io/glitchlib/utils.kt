package io.glitchlib

import io.glitchlib.auth.CachedStorage
import java.util.Properties

private val gitProperties = Properties().apply {
    load(GlitchClient::class.java.getResourceAsStream("git.properties"))
}

/**
 * The default `User-Agent` will be like this: `Glitch (v1.0.0/C3F5A875) - https://glitchlib.io`
 */
internal val DEFAULT_USER_AGENT = gitProperties.let {
    "${it.getProperty("application.name").capitalize()} (v${it.getProperty("application.version")}/${it.getProperty("git.commit.id.abbrev").toUpperCase()}) - ${it.getProperty(
        "application.url"
    )}"
}

internal val DEFAULT_STORAGE = CachedStorage()

typealias GlitchException = RuntimeException

enum class GlitchUrl(private val baseUrl: String) {
    KRAKEN(GlitchUrl.API.baseUrl + "/kraken"),
    HELIX(GlitchUrl.API.baseUrl + "/helix"),
    API("https://api.twitch.tv"),
    ID("https://id.twitch.tv/ouath2");

    fun compose(endpoint: String): String = "$baseUrl${if (endpoint.startsWith("/")) endpoint else "/$endpoint"}"
}

/**
 * Marking some methods or endpoint as Unofficial.
 * They are not documented yet into [Twitch Developer Site](https://dev.twitch.tv/).
 * They will be strictly documented, so you will using it into your own purpose.
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
annotation class Unofficial(
    /**
     * Source of endpoints. If it is empty. Explanation is documented by [this annotation][Unofficial]
     * @return Explanation or source of unofficial endpoint.
     */
    val source: String = ""
)