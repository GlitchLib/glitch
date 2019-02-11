package glitch.auth.objects.json.impl

import glitch.auth.GlitchScope
import glitch.auth.objects.json.Validate
import java.util.*

data class ValidateImpl(
        override val clientId: String,
        override val login: String,
        override val scopes: Set<GlitchScope>,
        override val userId: Long
) : Validate {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is ValidateImpl) return false
        val validate = o as ValidateImpl?
        return clientId == validate!!.clientId &&
                login == validate.login &&
                scopes == validate.scopes &&
                userId == validate.userId
    }

    override fun hashCode(): Int {
        return Objects.hash(clientId, login, scopes, userId)
    }

    override fun toString(): String {
        return "Validate{" +
                "clientId='" + clientId + '\''.toString() +
                ", login='" + login + '\''.toString() +
                ", scopes=" + scopes +
                ", userId=" + userId +
                '}'.toString()
    }
}
