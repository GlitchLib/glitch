package io.glitchlib.auth

import io.glitchlib.GlitchException
import io.reactivex.Completable
import io.reactivex.Maybe

class CachedStorage internal constructor(
    private val credentials: MutableCollection<Credential> = mutableSetOf()
) : IAuthorize.IStorage {
    private var _app: AppCredential? = null

    override var appCredential: AppCredential
        get() = checkNotNull(_app) { "Applicaction Credential is not register" }
        set(value) {
            _app = value
        }

    override fun isEmpty() = credentials.isEmpty()

    override fun get(id: Long): Maybe<Credential> = Maybe.create {
        val cred = credentials.firstOrNull { it.id == id }
        if (cred != null)
            it.onSuccess(cred)
        else
            it.onError(GlitchException("There is no user of this ID: $id"))
    }

    override fun add(credential: Credential): Completable = Completable.create {
        // replace existed credential
        if (credentials.any { c -> c.id == credential.id }) {
            delete(credential.id)
        }
        if (credentials.add(credential)) {
            it.onComplete()
        } else {
            it.onError(GlitchException("Entry is already exist!"))
        }
    }

    override fun delete(id: Long): Completable = deleteIf { it.id == id }.onErrorResumeNext {
        return@onErrorResumeNext Completable.error(
            if (it is GlitchException) GlitchException(
                "ID not found: $id",
                it
            ) else it
        )
    }

    override fun deleteIf(condition: (Credential) -> Boolean): Completable = Completable.create { sink ->
        var completed: Boolean = false
        credentials.forEach {
            if (condition(it)) {
                credentials.remove(it)
                completed = true
                sink.onComplete()
            }
        }
        if (!completed)
            sink.onError(GlitchException("Current condition doesn't even catch existing requirements."))
    }

    override fun drop(): Completable = Completable.create {
        if (_app == null) it.onError(NullPointerException("App Credentials is already cleared"))
        else {
            _app = null
            it.onComplete()
        }
    }
}