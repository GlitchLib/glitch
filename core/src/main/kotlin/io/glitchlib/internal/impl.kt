package io.glitchlib.internal

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import io.glitchlib.GlitchClient
import io.glitchlib.GlitchUrl
import io.glitchlib.IConfig
import io.glitchlib.auth.Credential
import io.glitchlib.auth.GlobalUserState
import io.glitchlib.auth.IAuthorize
import io.glitchlib.auth.Scope
import io.glitchlib.internal.auth.AuthorizeImpl
import io.glitchlib.internal.http.HttpClient
import io.glitchlib.internal.http.bodyFlowable
import io.glitchlib.internal.http.bodySingle
import io.glitchlib.model.ChatRoom
import io.glitchlib.model.GlitchObject
import io.glitchlib.model.ImplementationSerializerAdapter
import io.glitchlib.model.OrdinalList
import io.glitchlib.pubsub.Topic
import io.glitchlib.pubsub.TopicInitializer
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import java.lang.reflect.Type

class GlitchClientImpl internal constructor(builder: GlitchClient.Builder) : ServiceMediator(), GlitchClient {

    override var settings: ConfigImpl = ConfigImpl(
        builder.clientId,
        builder.clientSecret,
        builder.defaultScope,
        null,
        LinkedHashSet(50),
        builder.channels,
        builder.isConnectionSecure
    )

    val http = HttpClient(builder.httpClient.apply {
        addInterceptor {
            it.proceed(it.request().newBuilder().apply {
                addHeader("User-Agent", builder.userAgent)
            }.build())
        }
    }.build(), builder.gson.apply {
        registerTypeAdapter(GlitchClient::class.java, object : JsonDeserializer<GlitchClient> {
            override fun deserialize(s1: JsonElement, s2: Type, s3: JsonDeserializationContext) = this@GlitchClientImpl
        })
        registerTypeAdapter(Any::class.java, ImplementationSerializerAdapter<Any>())
    }.create())

    private val _auth = AuthorizeImpl(this, builder.storage)

    init {
        if (builder.botUser != null) {
            _auth.create(builder.botUser!!).subscribe(Consumer<Credential> {
                this.settings.botUser = it
            })
        }

        if (builder.topics.isNotEmpty()) {
            Flowable.fromIterable(builder.topics)
                .flatMap { topic ->
                    topic.toRealTopic().toFlowable()
                }.subscribe {
                    this.settings.topics += it
                }
        }
    }

    override fun authorize(): IAuthorize = _auth

    fun registerService(service: GlitchObject) = super.register(service)

    override fun getChatUser(id: Long): Single<GlobalUserState> =
        http.get<GlobalUserState>(GlitchUrl.KRAKEN.compose("/users/$id/chat")) {
            addHeaders("Accept", "application/vnd.twitchtv.v5+json")
        }.bodySingle

    override fun getChatRooms(id: Long) =
        http.get<OrdinalList<ChatRoom>>(GlitchUrl.KRAKEN.compose("/chat/$id/rooms")).bodyFlowable

    private fun TopicInitializer.toRealTopic(): Single<Topic> =
        if (token != null) _auth.create(this.token).map {
            return@map this.toTopic(it)
        } else Single.just(this.toTopic(null))
}


data class ConfigImpl internal constructor(
    override val clientId: String,
    override val clientSecret: String,
    override val defaultScope: Collection<Scope>,
    override var botUser: Credential?,
    override val topics: MutableCollection<Topic>,
    override val channels: Collection<String>,
    override val isConnectionSecure: Boolean
) : IConfig