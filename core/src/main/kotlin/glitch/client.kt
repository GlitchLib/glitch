package glitch

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */

fun createClient(builder: GlitchClient.Builder.() -> Unit): GlitchClient = GlitchClient.builder().apply(builder).build()