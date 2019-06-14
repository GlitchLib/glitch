package io.glitchlib.internal.http

import io.glitchlib.model.OrdinalList
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

val <T> Single<HttpResponse<T>>.bodySingle: Single<T>
    get() = body.toSingle()

val <T> Single<HttpResponse<T>>.body: Maybe<T>
    get() = flatMapMaybe { it.body }

val <T, C : OrdinalList<T>> Single<HttpResponse<C>>.bodyFlowable: Flowable<T>
    get() = body.flattenAsFlowable { it.data }

val <T, C : OrdinalList<T>> Single<HttpResponse<C>>.bodySingleFirst: Single<T>
    get() = bodySingle.map { it.data[0] }

val Single<HttpResponse<Unit>>.completed: Completable
    get() = body.ignoreElement()

typealias AbstractRequest = () -> (HttpRequest.() -> Unit)