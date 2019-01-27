package glitch.auth.objects.json.impl

import com.google.gson.annotations.SerializedName
import glitch.auth.GlitchScope
import glitch.auth.objects.json.AccessToken
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Objects

data class AccessTokenImpl(
        override val accessToken: String,
        override val refreshToken: String,
        @field:SerializedName("expires_in")
        override val expiredAt: Instant = Instant.now().plus(60, ChronoUnit.DAYS),
        @field:SerializedName("scope")
        override val scopes: Set<GlitchScope>
) : AccessToken
