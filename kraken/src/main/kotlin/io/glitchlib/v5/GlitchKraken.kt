package io.glitchlib.v5

import io.glitchlib.GlitchClient
import io.glitchlib.model.GlitchObject
import io.glitchlib.v5.service.BitsService
import io.glitchlib.v5.service.ChannelService
import io.glitchlib.v5.service.ChatService
import io.glitchlib.v5.service.ClipService
import io.glitchlib.v5.service.CollectionService
import io.glitchlib.v5.service.GameService
import io.glitchlib.v5.service.IngestService
import io.glitchlib.v5.service.SearchService
import io.glitchlib.v5.service.StreamService
import io.glitchlib.v5.service.TeamService
import io.glitchlib.v5.service.UserService
import io.glitchlib.v5.service.VideoService

class GlitchKraken(
    override val client: GlitchClient
) : GlitchObject {

    val bits
        get() = BitsService(client)
    val channels
        get() = ChannelService(client)
    val chat
        get() = ChatService(client)
    val clips
        get() = ClipService(client)
    val collections
        get() = CollectionService(client)
    val games
        get() = GameService(client)
    val ingests
        get() = IngestService(client)
    val search
        get() = SearchService(client)
    val streams
        get() = StreamService(client)
    val teams
        get() = TeamService(client)
    val users
        get() = UserService(client)
    val videos
        get() = VideoService(client)

}