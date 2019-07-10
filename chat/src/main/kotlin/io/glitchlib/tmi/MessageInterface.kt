package io.glitchlib.tmi

import io.glitchlib.GlitchClient
import io.glitchlib.GlitchException
import io.glitchlib.internal.GlitchClientImpl
import io.glitchlib.internal.ws.GlitchSocketObject
import io.glitchlib.model.OpenEvent
import io.glitchlib.model.PingEvent
import io.glitchlib.tmi.event.Events
import io.glitchlib.tmi.event.IRCEvent
import io.glitchlib.tmi.internal.ChannelImpl
import io.glitchlib.tmi.internal.ChatRoomImpl
import io.glitchlib.tmi.internal.UserDirectMessage
import io.glitchlib.tmi.irc.IrcFormatter
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.SingleSource
import java.util.UUID

class MessageInterface(client: GlitchClient) : GlitchSocketObject(
    client as GlitchClientImpl,
    "ws${if (client.settings.isConnectionSecure) "s" else ""}://irc-ws.chat.twitch.tv",
    IrcFormatter(client)
) {
    internal val _channels = mutableSetOf<Channel>()
    internal val _chatRooms = mutableSetOf<ChatRoom>()

    val channels: Collection<Channel>
        get() = _channels

    val chatRooms: Collection<ChatRoom>
        get() = _chatRooms

    init {
        if (client.settings.botUser == null) {
            throw GlitchException("Cannot initialize message interface without credentials for bot!")
        }
        // Converting to current supported events
        onEvent<IRCEvent>().subscribe {
            Events.from(it, eventSubject::onNext)
        }
        // Keeps Connection Alive
        onEvent<PingEvent>().subscribe {
            sendRaw(Flowable.just("PONG :tmi.twitch.tv"))
        }

        onEvent<OpenEvent>().subscribe {
            if (_client.settings.channels.isNotEmpty()) {
                _client.settings.channels.forEach {
                    if (it.contains(':')) {
                        it.split(':').let {
                            client.getChatRooms(it[0].toLong()).filter { r ->
                                r.id == UUID.fromString(it[1])
                            }.flatMapCompletable {
                                this.sendJoin(it)
                            }.subscribe()
                        }
                    } else {
                        this.sendJoin(it).subscribe()
                    }
                }
            } else {
                client.settings.botUser!!.username.let {
                    this.join(it)
                }.subscribe()
            }
        }
    }

    fun join(channel: String): Single<Channel> =
        Flowable.fromIterable(_channels).filter { it.name == channel }.firstElement()
            .switchIfEmpty(SingleSource<Channel> { sink ->
                sink.onSubscribe(this.sendJoin(channel)
                    .andThen {
                        _client.settings.channels.add(channel)
                        it.onComplete()
                    }.andThen(getGlobalUserState(channel))
                    .map {
                        ChannelImpl(this, it)
                    }.doOnError {
                        sink.onError(it)
                    }.doOnSuccess {
                        _channels.add(it)
                        sink.onSuccess(it)
                    }.subscribe()
                )
            })

    fun join(chatRoom: io.glitchlib.model.ChatRoom): Single<ChatRoom> =
        Flowable.fromIterable(_chatRooms).filter { it.id == chatRoom.id }.firstElement()
            .switchIfEmpty(SingleSource<ChatRoom> { sink ->
                sink.onSubscribe(sendJoin(chatRoom)
                    .andThen(SingleSource<ChatRoom> {
                        it.onSuccess(ChatRoomImpl(this, chatRoom))
                    })

                    .doOnError {
                        sink.onError(it)
                    }.subscribe()
                )
            })

    fun directMessage(user: String): Single<UserDM> =
        getGlobalUserState(user).map { UserDirectMessage(this, it) }

    fun getChannel(channel: String): Maybe<Channel> =
        Flowable.fromIterable(_channels).filter { it.name == channel }.firstElement()

    fun getChatRooms(channel: String): Flowable<ChatRoom> = getChannel(channel).flatMapPublisher {
        it.rooms.map { r -> _chatRooms.first { it.id == r.id } }
    }

    fun getChatRoom(id: String): Maybe<ChatRoom> = getChatRoom(UUID.fromString(id))
    fun getChatRoom(id: UUID): Maybe<ChatRoom> = Flowable.fromIterable(_chatRooms).filter { it.id == id }.firstElement()
    internal fun sendPrivMsg(username: String, message: String) =
        _channels.first().send("/w $username $message")

    private fun sendJoin(channel: String) =
        sendRaw(Flowable.just("JOIN #$channel"))

    private fun sendJoin(chatRoom: io.glitchlib.model.ChatRoom) =
        sendJoin("chatrooms:${chatRoom.ownerId}:${chatRoom.id}")

    internal fun sendPart(channel: String) =
        sendRaw(Flowable.just("PART #$channel")).doOnComplete {
            _channels.removeIf { it.name == channel }
            _client.settings.channels.remove(channel)
        }

    internal fun sendPart(chatRoom: io.glitchlib.model.ChatRoom) =
        sendRaw(Flowable.just("PART #chatrooms:${chatRoom.ownerId}:${chatRoom.id}")).doOnComplete {
            _chatRooms.removeIf { it.id == chatRoom.id }
            _client.settings.channels.remove("chatrooms:${chatRoom.ownerId}:${chatRoom.id}")
        }

    private fun getGlobalUserState(login: String) =
        client.getUserId(login)
            .onErrorReturn {
                throw GlitchException("Cannot get this login $login!", it)
            }.flatMapSingle { client.getChatUser(it) }

    internal fun sendChannelMsg(channel: String, message: String): Completable =
        sendRaw(Flowable.just("PRIVMSG #$channel :$message"))

    internal fun sendDM(login: String, message: String): Completable = Flowable.fromIterable(_channels)
        .firstElement().flatMapCompletable { it.send("/w $login $message") }
}