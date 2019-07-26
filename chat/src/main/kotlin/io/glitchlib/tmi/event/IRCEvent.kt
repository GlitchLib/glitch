package io.glitchlib.tmi.event

import io.glitchlib.GlitchClient
import io.glitchlib.model.IEvent
import io.glitchlib.tmi.irc.Command
import io.glitchlib.tmi.irc.Prefix
import io.glitchlib.tmi.irc.Tags
import java.util.Date
import java.util.UUID

/**
 *
 * @param raw Raw Message, formatted into IRC
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class IRCEvent(
    override val client: GlitchClient,
    /**
     * Specific IRC Command. If some commands isn't match of [commands][Command] will be return [{][Command.UNKNOWN]
     *
     * @return IRC Command
     */
    val command: Command,

    /**
     * Prefix of IRC Message
     *
     * @return IRC Prefix
     */
    val prefix: Prefix,

    /**
     * Tags parameters. Will be empty if some [Tags] doesn't even support it.
     *
     * @return Tag object
     */
    val tags: Tags = Tags(emptyMap()),

    /**
     * Middle parameters. Usually contains channel name.
     *
     * @return Middle parameters in [Immutable List][java.util.List]
     */
    val middle: List<String> = emptyList(),

    /**
     * The leftover parameters after [Middle Parameters][middle] splitted with double dot colon (`:`). Generally it is a message of the channel, user or server.
     *
     * @return the leftover middle parameters splitted with double dot colon (`:`)
     */
    val trailing: String? = null

) : IEvent {
    override val eventId: UUID = UUID.fromString(tags["id"]) ?: UUID.randomUUID()
    override val createdAt: Date = tags.sentTimestamp

    val isActionMessage = trailing != null && trailing.matches(Regex("^\\001ACTION(.*)\\001$"))

    val formattedTrailing = trailing?.replace("\u0001ACTION", "")?.replace("\u0001", "")
}