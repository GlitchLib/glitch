package glitch.auth.objects.json

import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.Creation
import glitch.auth.GlitchScope

interface AccessToken : Creation {
    val accessToken: String

    val refreshToken: String

    @get:SerializedName("scope")
    val scopes: Set<GlitchScope>
}
