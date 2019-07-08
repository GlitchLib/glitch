package io.glitchlib.v5.model.json

import io.glitchlib.model.IDObject

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Cheermote(
        val prefix: String,
        val tiers: List<Tier>
) {

    /**
     *
     * @author Damian Staszewski [damian@stachuofficial.tv]
     * @version %I%, %G%
     * @since 1.0
     */
    data class Tier(
            override val id: Long,
            val color: String,
            val images: Map<Background, Map<Type, Map<Size, String>>>
    ) : IDObject<Long> {
        fun getImage(background: Background, type: Type, size: Size) =
                images.getValue(background).getValue(type).getValue(size)
    }

    /**
     *
     * @author Damian Staszewski [damian@stachuofficial.tv]
     * @version %I%, %G%
     * @since 1.0
     */
    enum class Background {
        LIGHT,
        DARK
    }

    /**
     *
     * @author Damian Staszewski [damian@stachuofficial.tv]
     * @version %I%, %G%
     * @since 1.0
     */
    enum class Type {
        STATIC,
        ANIMATED
    }

    /**
     *
     * @author Damian Staszewski [damian@stachuofficial.tv]
     * @version %I%, %G%
     * @since 1.0
     */
    enum class Size(private val value: Double) {
        X1(1.0),
        X15(1.5),
        X2(2.0),
        X3(3.0),
        X4(4.0);


        companion object {
            fun of(size: Double): Size {
                return values().firstOrNull { it.value == size }
                        ?: throw NullPointerException("Cannot obtain requested size: $size")
            }
        }
    }
}
