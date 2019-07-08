package io.glitchlib.v5.service

import io.glitchlib.GlitchClient
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.v5.internal.AbstractKrakenService
import io.glitchlib.v5.internal.model.json.Cheermotes
import io.glitchlib.v5.model.json.Cheermote
import io.reactivex.Flowable

class BitsService internal constructor(client: GlitchClient) : AbstractKrakenService(client) {
    fun getCheermotes(channelId: Long? = null): Flowable<Cheermote> =
            get<Cheermotes>("/bits/actions") {
                if (channelId != null) {
                    addQueryParameters("channel_id", channelId.toString())
                }
            }.bodyFlowable
}
