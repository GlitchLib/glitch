package io.glitchlib

import com.google.gson.GsonBuilder
import io.glitchlib.auth.Credential
import io.glitchlib.auth.GlobalUserState
import io.glitchlib.auth.IAuthorize
import io.glitchlib.auth.Scope
import io.glitchlib.auth.Token
import io.glitchlib.internal.GlitchClientImpl
import io.glitchlib.model.ChatRoom
import io.glitchlib.pubsub.Topic
import io.glitchlib.pubsub.TopicInitializer
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient

interface GlitchClient {
    val settings: IConfig

    fun authorize(): IAuthorize

    fun getChatUser(id: Long): Single<GlobalUserState>

    fun getChatRooms(id: Long): Flowable<ChatRoom>

    fun getChatUser(credential: Credential) = getChatUser(credential.id)

    fun getChatRooms(credential: Credential) = getChatRooms(credential.id)

    fun getUserId(login: String): Maybe<Long>

    class Builder internal constructor() {
        internal lateinit var clientId: String
        internal lateinit var clientSecret: String
        internal val defaultScope = mutableSetOf<Scope>()
        internal var botUser: Token? = null
        internal val topics = LinkedHashSet<TopicInitializer>(50) as MutableSet<TopicInitializer>
        internal val channels = mutableSetOf<String>()
        internal var userAgent = DEFAULT_USER_AGENT
        internal var storage = DEFAULT_STORAGE
        internal val gson = GsonBuilder()
        internal val httpClient = OkHttpClient.Builder()
        internal var subscribeScheduler: Scheduler = Schedulers.io()
        internal var observerScheduler: Scheduler = Schedulers.computation()
        internal var isConnectionSecure: Boolean = true

        fun clientId(clientId: String) = apply {
            this.clientId = clientId
        }

        fun clientSecret(clientSecret: String) = apply {
            this.clientSecret = clientSecret
        }

        fun defaultScope(vararg defaultScope: Scope) = apply {
            this.defaultScope.addAll(defaultScope)
        }

        fun botUser(botUser: Token) = apply {
            this.botUser = botUser
        }

        fun topic(vararg topic: TopicInitializer) = apply {
            this.topics.addAll(topic)
        }

        fun channels(vararg channels: String) = apply {
            this.channels.addAll(channels)
        }

        fun userAgent(userAgent: String) = apply {
            this.userAgent = userAgent
        }

        fun connectionSecure(connectionSecure: Boolean) = apply {
            this.isConnectionSecure = connectionSecure
        }


        fun subscribeScheduler(subscribeScheduler: Scheduler) = apply {
            this.subscribeScheduler = subscribeScheduler
        }

        fun observerScheduler(observerScheduler: Scheduler) = apply {
            this.observerScheduler = observerScheduler
        }

        fun build(): GlitchClient = GlitchClientImpl(this)
    }

    companion object {
        @JvmStatic
        fun builder() = Builder()

        @GlitchDsl
        operator fun invoke(builder: Builder.() -> Unit) = builder().apply(builder).build()
    }
}

interface IConfig {
    val clientId: String
    val clientSecret: String
    val defaultScope: Collection<Scope>
    val botUser: Credential?
    val topics: Collection<Topic>
    val channels: Collection<String>
    val isConnectionSecure: Boolean
    val subscribeScheduler: Scheduler
    val observerScheduler: Scheduler
}
