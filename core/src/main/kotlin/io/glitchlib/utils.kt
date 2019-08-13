package io.glitchlib

import io.glitchlib.auth.CachedStorage
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.util.Properties

@DslMarker
annotation class GlitchDsl

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

object URL {
    val API = "https://api.twitch.tv".toHttpUrl()
    val OAUTH = "https://id.twitch.tv/ouath2".toHttpUrl()
    val KRAKEN = API.newBuilder().addPathSegment("kraken").build()
    val HELIX = API.newBuilder().addPathSegment("helix").build()
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