package io.glitchlib.v5.service

import io.glitchlib.GlitchClient
import io.glitchlib.Unofficial
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.model.OrdinalList
import io.glitchlib.v5.internal.AbstractKrakenService
import io.glitchlib.v5.model.json.Channel
import io.glitchlib.v5.model.json.ChatBadges
import io.glitchlib.v5.model.json.Emote
import io.glitchlib.v5.model.json.EmoteSets
import io.glitchlib.v5.model.json.User

class ChatService(client: GlitchClient) : AbstractKrakenService(client) {

    val allEmoticons
        get() = get<OrdinalList<Emote>>("/chat/emoticons").bodyFlowable

    fun getChatBadges(id: Long) =
            get<ChatBadges>("/chat/$id/badges").bodySingle

    fun getChatEmoteSets(vararg ids: Int) = getChatEmoteSets(ids.toList())

    fun getChatEmoteSets(ids: Collection<Int>) =
            get<EmoteSets>("/chat/emoticon_images") {
                addQueryParameters("emotesets", ids.joinToString(",") { it.toString() })
            }.bodySingle

    fun getChatRooms(id: Long) = client.getChatRooms(id)

    fun getChatRooms(user: Channel) = client.getChatRooms(user.id)

    @Unofficial
    fun getChatUserState(user: User) = getChatUserState(user.id)

    @Unofficial
    fun getChatUserState(id: Long) = client.getChatUser(id)
}
