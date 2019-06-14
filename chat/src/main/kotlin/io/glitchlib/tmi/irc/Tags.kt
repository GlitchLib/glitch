package io.glitchlib.tmi.irc

import io.glitchlib.model.Badge
import io.glitchlib.model.UserType
import java.awt.Color
import java.util.Date
import java.util.Locale

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 0.1.0
 */
class Tags(private val tags: Map<String, String?>) : Map<String, String?> by tags {

    private val booleanKeys =
        arrayOf("turbo", "mod", "subscriber", "emote-only", "r9k", "subs-only", "msg-param-should-share-streak")

    fun getOrDefault(key: String, defaultValue: String) = get(key) ?: defaultValue

    fun getBoolean(key: String) = booleanKeys.any { it.equals(key, true) }
            && getOrDefault(key, "0") == "1"

    fun getInteger(key: String): Int = getOrDefault(key, "0").toInt()
    fun getLong(key: String): Long = getOrDefault(key, "0").toLong()

    val badges
        get() = get("badges")?.split(',')
            ?.map { Badge(it.split('/')[0], it.split('/')[1].toInt()) }
            ?.toSet()
            .orEmpty()

    val emotes
        get() = get("emotes")?.split('/')
            ?.map {
                val index = it.split(';')
                val pairSet = index[1].split(',').map { it.split('-') }
                    .map { it.first().toInt() to it.last().toInt() }.toSet()
                return@map Emote(index[0].toInt(), pairSet)
            }?.toSet().orEmpty()

    val emoteSets
        get() = get("emote-sets")?.split(',')
            ?.map { it.toInt() }
            ?.toSet()
            .orEmpty()

    val sentTimestamp
        get() = if (containsKey("tmi-sent-ts") && get("tmi-sent-ts") != null) Date(get("tmi-sent-ts")!!.toLong()) else Date()

    val broadcasterLanguage
        get() = if (containsKey("broadcast-lang") && get("broadcast-lang") != null)
            Locale.forLanguageTag(get("broadcast-lang")) else null

    val userType: UserType
        get() = if (containsKey("user-type") && get("user-type") != null)
            UserType.from(get("broadcast-lang")) else UserType.USER

    val color
        get() = if (containsKey("color") && get("color") != null)
            Color.decode(get("color")) else null

    override fun toString() = tags.toString()
}

