package glitch.chat

import glitch.auth.objects.json.Credential

data class Configuration(
        val botCredentials: Credential,
        val isDisableAutoPing: Boolean = false
)
