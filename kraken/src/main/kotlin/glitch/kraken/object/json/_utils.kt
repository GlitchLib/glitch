package glitch.kraken.`object`.json

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Image(
        val large: String,
        val medium: String,
        val small: String,
        val template: String
) {
    fun getCustomSize(width: Int, height: Int): String {
        return template.replace("{width}", Integer.toString(width))
                .replace("{height}", Integer.toString(height))
    }
}