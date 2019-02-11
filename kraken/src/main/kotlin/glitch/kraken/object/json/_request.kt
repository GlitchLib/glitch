package glitch.kraken.`object`.json

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import glitch.kraken.`object`.adapters.GameNameSerializer
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class ChannelBody(
        @SerializedName("status")
        val title: String?,
        @JsonAdapter(GameNameSerializer::class)
        val game: Game?,
        val delay: Int?,
        @SerializedName("channel_feed_enabled")
        val channelFeedEnabled: Boolean?
) {
    /**
     *
     * @author Damian Staszewski [damian@stachuofficial.tv]
     * @version %I%, %G%
     * @since 1.0
     */
    class Builder internal constructor() {
        internal constructor(body: ChannelBody) : this() {
            title = body.title
            game = body.game
            delay = body.delay
            channelFeedEnabled = body.channelFeedEnabled
        }

        var title: String? = null
            private set
        var game: Game? = null
            private set
        var delay: Int? = null
            private set
        var channelFeedEnabled: Boolean? = null
            private set

        fun setTitle(title: String?): Builder {
            this.title = title
            return this
        }

        fun setGame(game: Game?): Builder {
            this.game = game
            return this
        }

        fun setDelay(delay: Int?): Builder {
            this.delay = delay
            return this
        }

        fun setChannelFeedEnabled(channelFeedEnabled: Boolean?): Builder {
            this.channelFeedEnabled = channelFeedEnabled
            return this
        }

        fun build(): ChannelBody {
            return ChannelBody(title, game, delay, channelFeedEnabled)
        }
    }

    companion object {
        @JvmStatic
        fun builder() = Builder()

        @JvmStatic
        fun builder(body: ChannelBody) = Builder(body)

        fun of(builder: Builder.() -> Unit) = builder().apply(builder).build()
    }
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class VideoBody(
        val description: String?,
        @JsonAdapter(GameNameSerializer::class)
        val game: Game?,
        val language: Locale?,
        @SerializedName("tag_list")
        val tags: Set<String>?,
        val title: String?
) {
    /**
     *
     * @author Damian Staszewski [damian@stachuofficial.tv]
     * @version %I%, %G%
     * @since 1.0
     */
    class Builder internal constructor() {
        var description: String? = null
            private set
        var game: Game? = null
            private set
        var language: Locale? = null
            private set
        var tags: Set<String>? = null
            private set
        var title: String? = null
            private set

        fun setDescription(description: String): Builder {
            this.description = description
            return this
        }

        fun setGame(game: Game): Builder {
            this.game = game
            return this
        }

        fun setLanguage(language: Locale): Builder {
            this.language = language
            return this
        }

        fun setTags(tags: Set<String>): Builder {
            this.tags = tags
            return this
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun build(): VideoBody {
            return VideoBody(description, game, language, tags, title)
        }
    }

    companion object {
        @JvmStatic
        fun builder(): Builder {
            return Builder()
        }

        fun of(builder: Builder.() -> Unit) = builder().apply(builder).build()
    }
}