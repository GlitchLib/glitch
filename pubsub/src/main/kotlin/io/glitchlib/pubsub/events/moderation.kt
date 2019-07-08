package io.glitchlib.pubsub.events

import io.glitchlib.GlitchClient
import io.glitchlib.pubsub.Topic
import io.glitchlib.pubsub.model.json.ActivationByMod
import io.glitchlib.pubsub.model.json.Ban
import io.glitchlib.pubsub.model.json.Host
import io.glitchlib.pubsub.model.json.MessageDelete
import io.glitchlib.pubsub.model.json.Moderator
import io.glitchlib.pubsub.model.json.Timeout
import io.glitchlib.pubsub.model.json.Unban

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class TimeoutUserEvent(
        override val client: GlitchClient,
        override val topic: Topic,
        override val message: Timeout
) : PubSubMessageEvent<Timeout>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class UnbanUserEvent(
        override val client: GlitchClient,
        override val topic: Topic,
        override val message: Unban
) : PubSubMessageEvent<Unban>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class HostEvent(
        override val client: GlitchClient,
        override val topic: Topic,
        override val message: Host
) : PubSubMessageEvent<Host>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class SubscribersOnlyEvent(
        override val client: GlitchClient,
        override val topic: Topic,
        override val message: ActivationByMod
) : PubSubMessageEvent<ActivationByMod>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class ClearChatEvent(
        override val client: GlitchClient,
        override val topic: Topic,
        override val message: Moderator
) : PubSubMessageEvent<Moderator>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class EmoteOnlyEvent(
        override val client: GlitchClient,
        override val topic: Topic,
        override val message: ActivationByMod
) : PubSubMessageEvent<ActivationByMod>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class Robot9000Event(
        override val client: GlitchClient,
        override val topic: Topic,
        override val message: ActivationByMod
) : PubSubMessageEvent<ActivationByMod>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class BanUserEvent(
        override val client: GlitchClient,
        override val topic: Topic,
        override val message: Ban
) : PubSubMessageEvent<Ban>

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
class MessageDeleteEvent(
        override val client: GlitchClient,
        override val topic: Topic,
        override val message: MessageDelete
) : PubSubMessageEvent<MessageDelete>