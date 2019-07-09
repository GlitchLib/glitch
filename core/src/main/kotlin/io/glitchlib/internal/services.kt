package io.glitchlib.internal

import io.glitchlib.GlitchClient
import io.glitchlib.auth.Credential
import io.glitchlib.auth.Scope
import io.glitchlib.auth.ScopeIsMissingException
import io.glitchlib.model.GlitchObject
import io.glitchlib.model.IDObject
import io.reactivex.Single
import kotlin.reflect.KClass

abstract class ServiceMediator {
    private val services = mutableSetOf<GlitchObject>()

    inline fun <reified S : GlitchObject> use(): S? = use(S::class)

    fun <S : GlitchObject> use(type: KClass<S>): S? = use(type.java)

    @Suppress("UNCHECKED_CAST")
    fun <S : GlitchObject> use(type: Class<S>): S? =
        services.firstOrNull { it::class.java === type } as S?

    protected fun register(service: GlitchObject) {
        if (service !is IDObject<*>) {
            services.add(service)
        }
    }

    inline fun <reified S : GlitchObject> hasService() = hasService(S::class)

    fun <S : GlitchObject> hasService(type: KClass<S>) = hasService(type.java)

    fun <S : GlitchObject> hasService(type: Class<S>) = services.any { it::class.java === type }

    internal fun clear() {
        services.clear()
    }
}

abstract class AbstractService(
    override val client: GlitchClient
) : GlitchObject {
    protected fun Credential.scopeCheck(scope: Scope) = scopes.any { scope === it }
    protected inline fun <reified T> scopeIsMissing(scope: Scope) = Single.error<T>(ScopeIsMissingException(scope))
}