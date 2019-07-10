package io.glitchlib

import io.glitchlib.tmi.MessageInterface

val GlitchClient.messageInterface: MessageInterface
    get() = MessageInterface(this)