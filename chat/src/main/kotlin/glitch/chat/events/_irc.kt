package glitch.chat.events

import glitch.api.objects.enums.UserType
import glitch.api.objects.json.Badge
import glitch.api.objects.json.interfaces.IDObject
import glitch.api.ws.events.IEvent
import glitch.chat.GlitchChat
import java.awt.Color
import java.time.Instant
import java.util.*

private val booleanKeys = arrayOf("turbo", "mod", "subscriber", "emote-only", "r9k", "subs-only", "msg-param-should-share-streak")

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 0.1.0
 */
data class Prefix(val raw: String, val nick: String?, val user: String?, val host: String) {
    override fun toString(): String = raw

    companion object {
        /**
         * @param rawPrefix
         * @return
         */
        @JvmStatic
        fun fromRaw(rawPrefix: String): Prefix {
            if (!rawPrefix.matches(":(?:.*)tmi.twitch.tv".toRegex())) {
                throw IllegalArgumentException("The RAW Prefix is invalid! PREFIX: $rawPrefix")
            }
            val prefix = rawPrefix.substring(1)
            var nick: String? = null
            var user: String? = null
            val host: String


            if (prefix.contains("@")) {
                val nh = rawPrefix.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                host = nh[1]
                if (nh[0].contains("!")) {
                    val nu = nh[0].split("!".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    nick = nu[0]
                    user = nu[1]
                } else {
                    nick = nh[0]
                }
            } else {
                host = prefix
            }

            return Prefix(rawPrefix, nick, user, host)
        }
    }
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 0.1.0
 */
class Tags(tags : Map<String, String?>) : Map<String, String?> by tags {

    fun getOrDefault(key: String, defaultValue: String) = get(key) ?: defaultValue

    fun getBoolean(key: String) = booleanKeys.any { it.equals(key, true) }
            && getOrDefault(key, "0") == "1"

    fun getInteger(key: String):Int = getOrDefault(key, "0").toInt()
    fun getLong(key: String):Long = getOrDefault(key, "0").toLong()

    val badges = get("badges")?.split(',')
            ?.map { Badge(it.split('/')[0], it.split('/')[1].toInt()) }
            ?.toSet()
            .orEmpty()

    val emotes = get("emotes")?.split('/')
            ?.map {
                val index = it.split(':')
                val pairSet = index[1].split(',').map { it.split('-') }
                        .map { it[0].toInt() to it[1].toInt() }.toSet()
                return@map Emote(index[0].toInt(), pairSet)
            }.orEmpty()

    val emoteSets = get("emote-sets")?.split(',')
            ?.map { it.toInt() }
            .orEmpty()

    val sentTimestamp = if (containsKey("tmi-sent-ts") && get("tmi-sent-ts") != null) Date(get("tmi-sent-ts")!!.toLong()).toInstant() else Instant.now()

    val broadcasterLanguage = if (containsKey("broadcast-lang") && get("broadcast-lang") != null)
        Locale.forLanguageTag(get("broadcast-lang")) else null

    val userType: UserType = if (containsKey("user-type") && get("user-type") != null)
        UserType.from(get("broadcast-lang")) else UserType.USER

    val color = if (containsKey("color") && get("color") != null)
        Color.decode(get("color")) else null
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 0.1.0
 */
enum class Command {
    UNKNOWN,
    PRIV_MSG,
    NOTICE,
    PING,
    PONG,
    JOIN,
    PART,
    HOST_TARGET,
    CLEAR_CHAT,
    CLEAR_MESSAGE,
    USER_STATE,
    GLOBAL_USER_STATE,
    NICK,
    PASS,
    CAP,
    RPL_WELCOME,
    RPL_YOURHOST,
    RPL_CREATED,
    RPL_MYINFO,
    RPL_NAMREPLY,
    RPL_ENDOFNAMES,
    RPL_MOTD,
    RPL_MOTDSTART,
    RPL_ENDOFMOTD,
    ERR_UNKNOWNCOMMAND,
    WHISPER,
    ROOM_STATE,
    RECONNECT,
    SERVER_CHANGE,
    USER_NOTICE;

    internal var value = if (name.startsWith("RPL_")) name.substring(4) else name

    override fun toString(): String {
        return value
    }

    companion object {

        fun of(cmd: String): Command {
            when (cmd) {
                "PRIVMSG" -> return PRIV_MSG
                "NOTICE" -> return NOTICE
                "PING" -> return PING
                "PONG" -> return PONG
                "HOSTTARGET" -> return HOST_TARGET
                "CLEARCHAT" -> return CLEAR_CHAT
                "USERSTATE" -> return USER_STATE
                "GLOBALUSERSTATE" -> return GLOBAL_USER_STATE
                "NICK" -> return NICK
                "JOIN" -> return JOIN
                "PART" -> return PART
                "PASS" -> return PASS
                "CAP" -> return CAP
                "001" -> return RPL_WELCOME
                "002" -> return RPL_YOURHOST
                "003" -> return RPL_CREATED
                "004" -> return RPL_MYINFO
                "353" -> return RPL_NAMREPLY
                "366" -> return RPL_ENDOFNAMES
                "372" -> return RPL_MOTD
                "375" -> return RPL_MOTDSTART
                "376" -> return RPL_ENDOFMOTD
                "421" -> return ERR_UNKNOWNCOMMAND
                "WHISPER" -> return WHISPER
                "SERVERCHANGE" -> return SERVER_CHANGE
                "RECONNECT" -> return RECONNECT
                "ROOMSTATE" -> return ROOM_STATE
                "USERNOTICE" -> return USER_NOTICE
                "CLEARMSG" -> return CLEAR_MESSAGE
                else -> {
                    val com = UNKNOWN
                    com.value = cmd
                    return com
                }
            }
        }
    }
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 0.1.0
 */
data class Emote(
        override val id: Int,
        val indexRanges: Set<Pair<Int, Int>>
) : IDObject<Int> {

    fun getEmoteUrl(emoteSize: Emote.Size): String {
        return String.format("http://static-cdn.jtvnw.net/emoticons/v1/%d/%s", id, emoteSize.value)
    }

    enum class Size private constructor(internal val value: Double) {
        X1(1.0),
        X2(2.0),
        X3(3.0)
    }
}

/**
 *
 * @param raw Raw Message, formatted into IRC
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class IRCEvent(
        override val client: GlitchChat,
        val raw: String
) : IEvent<GlitchChat> {

    /**
     * Tags parameters. Will be empty if some [Tags] doesn't even support it.
     *
     * @return Tag object
     */
    val tags: Tags

    /**
     * Prefix of IRC Message
     *
     * @return IRC Prefix
     */
    val prefix: Prefix

    /**
     * Specific IRC Command. If some commands isn't match of [commands][Command] will be return [{][Command.UNKNOWN]
     *
     * @return IRC Command
     */
    val command: Command

    /**
     * Middle parameters. Usually contains channel name.
     *
     * @return Middle parameters in [Immutable List][java.util.List]
     */
    val middle: List<String>

    /**
     * The leftover parameters after [Middle Parameters][middle] splitted with double dot colon (`:`). Generally it is a message of the channel, user or server.
     *
     * @return the leftover middle parameters splitted with double dot colon (`:`)
     */
    val trailing: String?

    init {
        val split = raw.split(Regex("\\s:"), 3)
                .map { if (it.startsWith(':')) it.substring(1).trim() else it }
                .toMutableList()


        tags = Tags(if (split[0].startsWith('@')) {
            split[0].substring(1).trim().split(';').map {
                val p = it.split('=')
                return@map Pair(p[0], valueParse(p[1]))
            }.toMap()
        } else {
            emptyMap()
        })

        split[0] = split[1].trim()

        if (split.size > 2) {
            split[1] = split[2].trim()
        }

        trailing = split[1]

        val mid = split[0].split(Regex("\\s"), 3)
                .map { if (it.startsWith(":")) it.substring(1) else it }

        prefix = Prefix.fromRaw(":" + mid.first { it.matches(Regex("^(.+)(!.+)*?(@.+)*?$")) })
        command = Command.of(mid.first { it.matches(Regex("^([A-Z]+|[0-9]{1,3})")) })
        middle = mid.joinToString(" ").replace(prefix.raw, "")
                .replace(command.toString(), "").replace(":", "").trim()
                .split(' ')
    }

    val isActionMessage = trailing != null && trailing.matches(Regex("^\\001ACTION(.*)\\001$"))

    val formattedTrailing = trailing?.replace("\u0001ACTION", "")?.replace("\u0001", "")

    private fun valueParse(value: String?): String? {
        var v = value
        if (v != null) {
            if (v == "") return null
            if (v.contains("\\r")) v = v.replace("\\r", "\r")
            if (v.contains("\\n")) v = v.replace("\\n", "\n")
            if (v.contains("\\\\")) v = v.replace("\\\\", "\\")
            if (v.contains("\\s")) v = v.replace("\\s", " ")
            if (v.contains("\\:")) v = v.replace("\\:", ":")
        }

        return value
    }

    override fun toString(): String = raw
}

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class RoomState(
        override val id: Long,
        var broadcasterLanguage: Locale?,
        var isEmoteOnly: Boolean,
        var follow: Long,
        var isR9k: Boolean,
        var slow: Long,
        var isSubsOnly: Boolean
): IDObject<Long> {
    val isFollowersOnly: Boolean
        get() = follow != -1L

    val isSlowMode: Boolean
        get() = slow > 0
}