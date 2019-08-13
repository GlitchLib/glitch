package io.glitchlib.internal.http

import com.google.gson.Gson
import io.glitchlib.model.ErrorResponse
import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.Single
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException
import java.net.URLConnection

class HttpClient(
        @PublishedApi internal val httpClient: OkHttpClient,
        val gson: Gson,
        @PublishedApi internal val scheduleObserver: Scheduler,
        @PublishedApi internal val scheduleSubscriber: Scheduler
) {
    inline fun <reified T> get(url: HttpUrl, request: HttpRequest.() -> Unit = {}) =
            exchange<T>(url, HttpMethod.GET, request)

    inline fun <reified T> post(url: HttpUrl, request: HttpRequest.() -> Unit = {}) =
            exchange<T>(url, HttpMethod.POST, request)

    inline fun <reified T> put(url: HttpUrl, request: HttpRequest.() -> Unit = {}) =
            exchange<T>(url, HttpMethod.PUT, request)

    inline fun <reified T> patch(url: HttpUrl, request: HttpRequest.() -> Unit = {}) =
            exchange<T>(url, HttpMethod.PATCH, request)

    inline fun <reified T> delete(url: HttpUrl, request: HttpRequest.() -> Unit = {}) =
            exchange<T>(url, HttpMethod.DELETE, request)

    inline fun <reified T> options(url: HttpUrl, request: HttpRequest.() -> Unit = {}) =
            exchange<T>(url, HttpMethod.OPTIONS, request)

    inline fun <reified T> exchange(
            url: HttpUrl,
            method: HttpMethod = HttpMethod.GET,
            request: HttpRequest.() -> Unit = {}
    ): Single<HttpResponse<T>> =
            exchange(HttpRequest(url, method).apply(request))


    inline fun <reified T> exchange(request: HttpRequest): Single<HttpResponse<T>> =
            httpClient.newCall(request.toRequest()).toResponse<T>(request)
                    .subscribeOn(scheduleSubscriber)
                    .observeOn(scheduleObserver)

    @PublishedApi
    internal inline fun <reified T> Call.toResponse(request: HttpRequest): Single<HttpResponse<T>> =
            Single.create {
                enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        it.onError(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        HttpResponse<T>(request, response.toStatus(), response.headers.toMultimap(),
                                Maybe.unsafeCreate {
                                    if (response.code in 400..599) {
                                        it.onError(GlitchHttpException(request.url, response.body.mapTo<ErrorResponse>()!!))
                                    } else {
                                        it.onSuccess(response.body.mapTo<T>())
                                    }
                                }
                        )
                    }
                })
            }

    @PublishedApi
    internal fun HttpRequest.toRequest() = Request.Builder().apply {
        url(checkAndUseUrls(url, queryParams))
        method(method.name, this@toRequest.body.asRequestBody ?: method.defaultBody)
        headers.toPair().forEach {
            addHeader(it.first, it.second)
        }
    }.build()

    private fun checkAndUseUrls(
            url: HttpUrl,
            query: Map<String, List<String>>
    ): HttpUrl =
            url.newBuilder().apply {
                query.toPair().forEach {
                    addQueryParameter(it.first, it.second)
                }
            }.build()

    @PublishedApi
    internal fun Response.toStatus() = HttpStatus(code, message)

    @PublishedApi
    internal inline fun <reified T> ResponseBody?.mapTo(): T? =
            this@mapTo?.charStream()?.let { gson.fromJson(it, T::class.java) }

    private fun Any?.toJson(): String? = when (this) {
        null, "" -> null
        is String -> this
        else -> gson.toJson(this)
    }

    private val Any?.asRequestBody: RequestBody?
        get() = when (this) {
            null -> null
            is String -> toRequestBody("application/json".toMediaType())
            is RequestBody -> this
            is File -> asRequestBody(this.mimeType)
            else -> toJson().asRequestBody
        }

    private val File.mimeType: MediaType
        get() = URLConnection.getFileNameMap().getContentTypeFor(this.toURI().toString()).toMediaType()
}

enum class HttpMethod(val defaultBody: RequestBody?) {
    GET(null), POST(byteArrayOf().toRequestBody()),
    PUT(byteArrayOf().toRequestBody()), PATCH(byteArrayOf().toRequestBody()),
    DELETE(null), OPTIONS(null)
}

class HttpRequest(
        @PublishedApi
        internal val url: HttpUrl,
        internal val method: HttpMethod = HttpMethod.GET
) {
    internal val queryParams: MutableMap<String, List<String>> = mutableMapOf()
    internal val headers: MutableMap<String, List<String>> = mutableMapOf()
    internal var body: Any? = null
        private set

    fun addQueryParameters(key: String, vararg values: String) = apply {
        if (queryParams.containsKey(key)) {
            queryParams[key] = values.toMutableList() + (queryParams[key] ?: listOf())
        } else {
            queryParams[key] = values.toMutableList()
        }
    }

    fun addHeaders(key: String, vararg values: String) = apply {
        if (headers.containsKey(key)) {
            headers[key] = values.toMutableList() + (queryParams[key] ?: listOf())
        } else {
            headers[key] = values.toMutableList()
        }
    }

    fun setBody(body: Any) = apply {
        this.body = body
    }
}


class HttpResponse<T>
@PublishedApi internal constructor(
        protected val request: HttpRequest,
        val status: HttpStatus,
        val headers: Map<String, List<String>>,
        val body: Maybe<T>
) {

}

data class HttpStatus internal constructor(
        val code: Int,
        val message: String
) {
    val isSuccess: Boolean
        get() = code in 200..399

    val isError: Boolean
        get() = code in 400..599

    val isRedirect: Boolean
        get() = arrayOf(300, 301, 302, 303, 307, 308).contains(code)
}

internal fun <K, V> Map<K, List<V>>.toPair(): List<Pair<K, V>> =
        flatMap { e -> e.value.map { Pair(e.key, it) } }

class GlitchHttpException(val url: HttpUrl, val code: Int, val reason: String) :
        IOException("$code - $reason [from: $url]") {
    constructor(url: HttpUrl, error: ErrorResponse) : this(
            url,
            error.status,
            if (error.message.isNullOrEmpty()) error.error else error.message
    )
}
