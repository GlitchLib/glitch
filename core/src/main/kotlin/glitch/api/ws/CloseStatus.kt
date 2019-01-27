package glitch.api.ws

/**
 * Close Status Object
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class CloseStatus(val code: Int, val reason: String)
