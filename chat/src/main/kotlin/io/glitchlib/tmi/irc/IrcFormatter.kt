package io.glitchlib.tmi.irc

import io.glitchlib.GlitchClient
import io.glitchlib.model.IEvent
import io.glitchlib.tmi.event.IRCEvent

class IrcFormatter(private val client: GlitchClient) : (String) -> IEvent {
    override operator fun invoke(raw: String): IEvent {
        if (raw.contains(System.lineSeparator())) {
            for (r in raw.split(System.lineSeparator())) {
                return invoke(r)
            }
        }

        return parse(raw)
    }

    private fun parse(raw: String): IEvent {
        var pairLine = raw.split(" :", limit = 3)
            .mapIndexed { i, string -> if (i == 0 && string.startsWith("@")) string else ":$string" }
            .let {
                var (first, second, third) = it
                if (!first.startsWith("@")) {
                    third = second
                    second = first
                }

                return@let Triple(
                    if (first == second) null else first,
                    second,
                    if (third == ":") null else third
                )
            }

        val tags = Tags(
            pairLine.first?.substring(1)?.split(';')?.map {
                it.split('=', limit = 2).let {
                    Pair(
                        it.first(), it.last()
                            .replace("\\:", ";")
                            .replace("\\s", " ")
                            .replace("\\\\", "\\")
                            .replace("\\r", "\r")
                            .replace("\\n", "\n")
                    )
                }
            }?.toMap() ?: emptyMap()
        )

        val trailing = pairLine.third

        val m = pairLine.second.split(' ')

        var prefix = Prefix.empty()
        var command = Command.UNKNOWN
        val middle = if (m.isNotEmpty()) m.toMutableList() else mutableListOf()

        m.forEach { s ->
            when {
                s.startsWith(':') -> {
                    prefix = Prefix.fromRaw(s)
                    middle.remove(s)
                }
                s.matches(Regex("^([A-Z]+|[0-9]{3}|(^ACK))$")) -> {
                    command = Command.of(s)
                    middle.remove(s)
                }
            }
        }

        return IRCEvent(client, command, prefix, tags, middle, trailing)
    }

}
