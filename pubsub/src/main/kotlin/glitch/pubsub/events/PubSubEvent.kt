package glitch.pubsub.events

import com.google.gson.Gson
import com.google.gson.JsonObject
import glitch.api.ws.events.IEvent
import glitch.pubsub.GlitchPubSub

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class PubSubEvent(
        override val client: GlitchPubSub,
        val mapper: Gson,
        val data: JsonObject
) : IEvent<GlitchPubSub>