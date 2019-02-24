package glitch.chat.irc

import glitch.api.ws.IEventConverter
import glitch.api.ws.events.IEvent
import glitch.chat.GlitchChat
import glitch.chat.events.IRCEvent

class TmiConverter : IEventConverter<GlitchChat> {

    override fun convert(client: GlitchChat, raw: String): IEvent<GlitchChat> {
        if (raw.contains(System.lineSeparator())) {
            for (line in raw.split(System.lineSeparator())) {
                return convert(client, line)
            }
        }

        return parse(client, raw)
    }

    private fun parse(client: GlitchChat, line: String): IRCEvent {

        var pairLine = line.split(" :", limit = 3).mapIndexed { i, string -> if (i == 0) string else ":$string" }

        val tags = Tags(if (pairLine[0].startsWith('@')) pairLine[0].substring(1).split(";").map {
            val p = it.split('=')
            return@map Pair(p.first(), p.last()
                    .replace("\\:", ";")
                    .replace("\\s", " ")
                    .replace("\\\\", "\\")
                    .replace("\\r", "\r")
                    .replace("\\n", "\n"))
        }.toMap() else emptyMap())

        if (tags.isNotEmpty()) {
            pairLine = pairLine.drop(1)
        }

        val trailing = if (pairLine.size > 1) pairLine[1] else null

        val mid = pairLine[0].split(' ')

        var prefix = Prefix.empty()
        var command = Command.UNKNOWN
        var middle = if (mid.isEmpty()) emptyList() else mid

        mid.forEach { s ->
            when {
                s.startsWith(':') -> {
                    prefix = Prefix.fromRaw(s)
                    middle = middle.filter { it != s }
                }
                s.matches(Regex("^([A-Z]+|[0-9]{3}|(^ACK))$")) -> {
                    command = Command.of(s)
                    middle = middle.filter { it != s }
                }
            }
        }

        return IRCEvent(client, command, prefix, tags, middle, trailing)
    }

}