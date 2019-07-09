package io.glitchlib.api.model

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class UploadData(
        val url: String
)

data class Code(
        val code: String,
        val status: CodeStatus
)