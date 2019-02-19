package glitch.auth.objects.json.impl

import com.google.gson.annotations.SerializedName
import glitch.auth.GlitchScope
import glitch.auth.objects.json.AccessToken
import java.time.Instant
import java.time.temporal.ChronoUnit

data class AccessTokenImpl(
        override val accessToken: String,
        override val refreshToken: String,
        @SerializedName("scope")
        override val scopes: Set<GlitchScope>,
        override val createdAt: Instant = Instant.now()
) : AccessToken
