package glitch.chat.irc

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 0.1.0
 */
data class Prefix(val raw: String, val nick: String? = null, val user: String? = null, val host: String) {
    override fun toString(): String = raw

    companion object {
        /**
         * @param rawPrefix
         * @return
         */
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

        fun empty() = Prefix(":tmi.twitch.tv", host = "tmi.twitch.tv")
    }
}