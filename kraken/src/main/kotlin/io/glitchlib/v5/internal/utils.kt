package io.glitchlib.v5.internal

import io.glitchlib.GlitchClient
import io.glitchlib.GlitchUrl
import io.glitchlib.internal.AbstractService
import io.glitchlib.internal.GlitchClientImpl
import io.glitchlib.internal.http.HttpMethod
import io.glitchlib.internal.http.HttpRequest

abstract class AbstractKrakenService internal constructor(client: GlitchClient) : AbstractService(client) {
    protected inline fun <reified T> exchange(
            method: HttpMethod,
            endpoint: String,
            request: HttpRequest.() -> Unit = {}
    ) =
            (client as GlitchClientImpl).http.exchange<T>(
                    HttpRequest(GlitchUrl.KRAKEN.compose(endpoint), method)
                            .addHeaders("Client-ID", client.settings.clientId)
                            .addHeaders("Accept", "application/vnd.twitchtv.v5+json")
                            .apply(request)
            )

    protected inline fun <reified T> get(endpoint: String, request: HttpRequest.() -> Unit = {}) =
            exchange<T>(HttpMethod.GET, endpoint, request)

    protected inline fun <reified T> post(endpoint: String, request: HttpRequest.() -> Unit = {}) =
            exchange<T>(HttpMethod.POST, endpoint, request)

    protected inline fun <reified T> put(endpoint: String, request: HttpRequest.() -> Unit = {}) =
            exchange<T>(HttpMethod.PUT, endpoint, request)

    protected inline fun <reified T> patch(endpoint: String, request: HttpRequest.() -> Unit = {}) =
            exchange<T>(HttpMethod.PATCH, endpoint, request)

    protected inline fun <reified T> delete(endpoint: String, request: HttpRequest.() -> Unit = {}) =
            exchange<T>(HttpMethod.DELETE, endpoint, request)

    protected inline fun <reified T> options(endpoint: String, request: HttpRequest.() -> Unit = {}) =
            exchange<T>(HttpMethod.OPTIONS, endpoint, request)
}