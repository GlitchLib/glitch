package glitch

import glitch.api.objects.json.interfaces.OrdinalList
import glitch.service.AbstractRequest

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
fun createClient(builder: GlitchClient.Builder.() -> Unit): GlitchClient = GlitchClient.builder().apply(builder).build()


/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
fun <T : AbstractRequest<*, out OrdinalList<*>>> T.get(request:T.() -> Unit) = apply(request).get()