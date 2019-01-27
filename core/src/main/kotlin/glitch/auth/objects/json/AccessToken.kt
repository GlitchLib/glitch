package glitch.auth.objects.json

import com.google.gson.annotations.SerializedName
import glitch.auth.GlitchScope
import java.time.Instant

interface AccessToken {
    val accessToken: String

    val refreshToken: String

    @get:SerializedName("expires_in")
    val expiredAt: Instant

    @get:SerializedName("scope")
    val scopes: Set<GlitchScope>
}
