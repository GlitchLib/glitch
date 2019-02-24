package glitch.api.ws

import com.google.gson.JsonElement
import glitch.api.ws.events.IEvent

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class MessageEvent(
        override val client: WebSocketClient,
        val data: JsonElement
) : IEvent<WebSocketClient>
