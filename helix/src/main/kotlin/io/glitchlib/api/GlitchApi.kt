package io.glitchlib.api

import io.glitchlib.GlitchClient
import io.glitchlib.api.service.AnalyticsService
import io.glitchlib.api.service.BitsService
import io.glitchlib.api.service.ClipsService
import io.glitchlib.api.service.EntitlementsService
import io.glitchlib.api.service.GamesService
import io.glitchlib.api.service.StreamsService
import io.glitchlib.api.service.SubscriptionsService
import io.glitchlib.api.service.TagsService
import io.glitchlib.api.service.UsersService
import io.glitchlib.api.service.VideosService
import io.glitchlib.api.service.WebhooksService
import io.glitchlib.model.GlitchObject

class GlitchApi(
    override val client: GlitchClient
) : GlitchObject {

    val analytics
        get() = AnalyticsService(client)
    val bits
        get() = BitsService(client)
    val clips
        get() = ClipsService(client)
    val entitlements
        get() = EntitlementsService(client)
    val games
        get() = GamesService(client)
    val streams
        get() = StreamsService(client)
    val subscriptions
        get() = SubscriptionsService(client)
    val tags
        get() = TagsService(client)
    val users
        get() = UsersService(client)
    val videos
        get() = VideosService(client)
    val webhooks
        get() = WebhooksService(client)
}