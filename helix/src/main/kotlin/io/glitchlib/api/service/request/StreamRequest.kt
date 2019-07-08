package io.glitchlib.api.service.request

import io.glitchlib.internal.http.AbstractRequest
import io.glitchlib.internal.http.HttpRequest
import java.util.*
import kotlin.collections.Collection
import kotlin.collections.LinkedHashSet
import kotlin.collections.isNotEmpty
import kotlin.collections.map
import kotlin.collections.plusAssign
import kotlin.collections.toList
import kotlin.collections.toSet
import kotlin.collections.toTypedArray

class StreamRequest internal constructor() : AbstractRequest {
    private var limit: Int? = null
    private var after: String? = null
    private var before: String? = null

    private val communityId = LinkedHashSet<String>(100)
    private val gameId = LinkedHashSet<Long>(100)
    private val language = LinkedHashSet<Locale>(100)
    private val userId = LinkedHashSet<Long>(100)
    private val userLogin = LinkedHashSet<String>(100)

    fun limit(limit: Int) = apply {
        this.limit = limit
    }

    fun after(after: String) = apply {
        this.after = after
    }

    fun before(before: String) = apply {
        this.before = before
    }

    fun communityId(vararg communityId: String) = communityId(communityId.toSet())

    fun communityId(communityId: Collection<String>) = apply {
        this.communityId += communityId
    }

    fun gameId(vararg gameId: Long) = gameId(gameId.toSet())

    fun gameId(gameId: Collection<Long>) = apply {
        this.gameId += gameId
    }

    fun language(vararg language: Locale) = language(language.toSet())

    fun language(language: Collection<Locale>) = apply {
        this.language += language
    }

    fun userId(vararg userId: Long) = userId(userId.toSet())

    fun userId(userId: Collection<Long>) = apply {
        this.userId += userId
    }

    fun userLogin(vararg userLogin: String) = userLogin(userLogin.toSet())

    fun userLogin(userLogin: Collection<String>) = apply {
        this.userLogin += userLogin
    }

    override fun invoke(): HttpRequest.() -> Unit = {
        if (limit != null && limit!! in 1..100) addQueryParameters("first", limit!!.toString())
        if (after != null) addQueryParameters("after", after!!)
        if (before != null) addQueryParameters("before", before!!)

        if (communityId.isNotEmpty()) {
            addQueryParameters("community_id", *communityId.toList().subList(0, 99).toTypedArray())
        }

        if (gameId.isNotEmpty()) {
            addQueryParameters("game_id", *gameId.toList().subList(0, 99).map(Long::toString).toTypedArray())
        }

        if (language.isNotEmpty()) {
            addQueryParameters("language", *language.toList().subList(0, 99).map(Locale::getLanguage).toTypedArray())
        }

        if (userId.isNotEmpty()) {
            addQueryParameters("user_id", *userId.toList().subList(0, 99).map(Long::toString).toTypedArray())
        }

        if (userLogin.isNotEmpty()) {
            addQueryParameters("user_login", *userLogin.toList().subList(0, 99).toTypedArray())
        }
    }
}