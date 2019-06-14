package io.glitchlib.internal.http

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import com.google.common.collect.Multimaps
import com.google.gson.Gson
import io.glitchlib.model.ErrorResponse
import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URL
import java.net.URLConnection

class HttpClient(
    @PublishedApi internal val httpClient: OkHttpClient,
    val gson: Gson
) {
    inline fun <reified T> get(url: String, request: HttpRequest.() -> Unit = {}) =
        exchange<T>(url, HttpMethod.GET, request)

    inline fun <reified T> post(url: String, request: HttpRequest.() -> Unit = {}) =
        exchange<T>(url, HttpMethod.POST, request)

    inline fun <reified T> put(url: String, request: HttpRequest.() -> Unit = {}) =
        exchange<T>(url, HttpMethod.PUT, request)

    inline fun <reified T> patch(url: String, request: HttpRequest.() -> Unit = {}) =
        exchange<T>(url, HttpMethod.PATCH, request)

    inline fun <reified T> delete(url: String, request: HttpRequest.() -> Unit = {}) =
        exchange<T>(url, HttpMethod.DELETE, request)

    inline fun <reified T> options(url: String, request: HttpRequest.() -> Unit = {}) =
        exchange<T>(url, HttpMethod.OPTIONS, request)

    inline fun <reified T> exchange(
        url: String,
        method: HttpMethod = HttpMethod.GET,
        request: HttpRequest.() -> Unit = {}
    ): Single<HttpResponse<T>> =
        exchange(HttpRequest(url, method).apply(request))


    inline fun <reified T> exchange(request: HttpRequest): Single<HttpResponse<T>> =
        httpClient.newCall(request.toRequest()).toResponse()

    @PublishedApi
    internal inline fun <reified T> Call.toResponse(): Single<HttpResponse<T>> =
        Single.create<Response> {
            enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    it.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    it.onSuccess(response)
                }

            })
        }.toResponse()

    @PublishedApi
    internal inline fun <reified T> Single<Response>.toResponse(): Single<HttpResponse<T>> = map {
        val err =
            if (it.code() in 400..599) gson.fromJson(it.body()!!.charStream(), ErrorResponse::class.java) else null
        return@map HttpResponse<T>(
            err?.toStatus() ?: it.toStatus(),
            it.headers().toImmutableMultimap(),
            if (err != null) Maybe.error(
                GlitchHttpException(
                    it.request().url().url(),
                    err
                )
            ) else it.body().toMaybe<T>()
        )
    }

    @PublishedApi
    internal fun HttpRequest.toRequest() = Request.Builder().apply {
        url(checkAndUseUrls(url, queryParams))
        method(method.name, body.toRequestBody())
        headers.toList().forEach {
            addHeader(it.first, it.second)
        }
    }.build()

    private fun checkAndUseUrls(
        url: String,
        query: ArrayListMultimap<String, String>
    ): URL =
        URI.create(url).let {
            var newQuery: String? = it.query

            if (newQuery == null) {
                newQuery = query.toList().joinToString("&") { "${it.first}=${it.second}" }
            } else {
                newQuery += query.toList().joinToString("&", prefix = "&") { "${it.first}=${it.second}" }
            }
            return URI(it.scheme, it.authority, it.path, newQuery, it.fragment).toURL()
        }

    @PublishedApi
    internal fun Response.toStatus() = HttpStatus(code(), message())

    @PublishedApi
    internal fun Headers.toImmutableMultimap() = Multimaps.unmodifiableListMultimap(
        Multimaps.newListMultimap(toMultimap().mapValues { it.value as Collection<String> }) { listOf() }
    )

    @PublishedApi
    internal inline fun <reified T> ResponseBody?.toMaybe() = if (this != null) Maybe.create<T> {
        it.onSuccess(gson.fromJson(charStream(), T::class.java))
    } else Maybe.empty()

    private fun Any?.toJson(): String? = when (this) {
        null, "" -> null
        is String -> this
        else -> gson.toJson(this)
    }

    private fun Any?.toRequestBody(): RequestBody? = when (this) {
        null -> null
        is String -> RequestBody.create(MediaType.get("application/json"), this)
        is RequestBody -> this
        is File -> RequestBody.create(MediaType.get(this.mimeType), this)
        else -> RequestBody.create(MediaType.get("application/json"), gson.toJson(this))
    }

    private val File.mimeType: String
        get() = URLConnection.getFileNameMap().getContentTypeFor(this.toURI().toString())
}

enum class HttpMethod {
    GET, POST, PUT, PATCH, DELETE, OPTIONS
}

class HttpRequest(
    internal val url: String,
    internal val method: HttpMethod = HttpMethod.GET
) {
    internal val queryParams = ArrayListMultimap.create<String, String>()
    internal val headers = ArrayListMultimap.create<String, String>()
    internal var body: Any? = null
        private set

    fun addQueryParameters(key: String, vararg values: String) = apply {
        queryParams.putAll(key, values.toMutableList())
    }

    fun addHeaders(key: String, vararg values: String) = apply {
        headers.putAll(key, values.toMutableList())
    }

    fun setBody(body: Any) = apply {
        this.body = body
    }
}


data class HttpResponse<T>
@PublishedApi internal constructor(
    val status: HttpStatus,
    val headers: Multimap<String, String>,
    val body: Maybe<T>
)

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

internal fun <K, V> Multimap<K, V>.toList(): List<Pair<K, V>> =
    asMap().toList().flatMap { p -> p.second.map { Pair(p.first, it) } }

class GlitchHttpException(val url: URL, val code: Int, val reason: String) :
    IOException("$code - $reason [from: $url]") {
    constructor(url: URL, error: ErrorResponse) : this(
        url,
        error.status,
        if (error.message.isNullOrEmpty()) error.error else error.message
    )
}
