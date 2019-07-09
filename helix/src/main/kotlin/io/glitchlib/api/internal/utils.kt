package io.glitchlib.api.internal

import java.text.SimpleDateFormat
import java.util.*


internal fun Date.toRfc3339(): String =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(this)