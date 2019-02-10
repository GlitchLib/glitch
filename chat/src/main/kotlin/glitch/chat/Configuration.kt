package glitch.chat

import glitch.auth.objects.json.Credential
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2

data class Configuration(
        val botCredentials: Credential,
        val isDisableAutoPing: Boolean = false
)
