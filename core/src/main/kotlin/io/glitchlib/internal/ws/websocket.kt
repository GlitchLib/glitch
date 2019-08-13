package io.glitchlib.internal.ws

import io.glitchlib.GlitchClient
import io.glitchlib.internal.ConfigImpl
import io.glitchlib.internal.GlitchClientImpl
import io.glitchlib.model.CloseEvent
import io.glitchlib.model.GlitchObject
import io.glitchlib.model.IEvent
import io.glitchlib.model.OpenEvent
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.reactivestreams.Publisher
import java.nio.channels.AlreadyConnectedException
import java.nio.channels.NotYetConnectedException
import kotlin.reflect.KClass

abstract class GlitchSocketObject(
    protected val _client: GlitchClientImpl,
    url: String,
    private val messageFormatter: (String) -> IEvent
) : GlitchObject {
    override val client: GlitchClient
        get() = _client
    private var ws: WebSocket? = null
    private val request: Request = Request.Builder()
        .url(url).build()

    protected val settings: ConfigImpl
        get() = _client.settings

    protected val eventSubject: PublishSubject<IEvent> = PublishSubject.create()

    val isConnected: Boolean
            get() = ws != null

    fun connect(): Completable = Completable.create {
        if (isConnected) it.onError(AlreadyConnectedException())
        else {
            _client.http.httpClient.newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    this@GlitchSocketObject.ws = webSocket
                    this@GlitchSocketObject.eventSubject.onNext(OpenEvent(client))
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    this@GlitchSocketObject.eventSubject.onNext(CloseEvent(CloseEvent.Status(code, reason), client))
                    this@GlitchSocketObject.ws = null
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    this@GlitchSocketObject.eventSubject.onNext(messageFormatter(text))
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    this@GlitchSocketObject.eventSubject.onError(t)
                    if (this@GlitchSocketObject.ws != null) {
                        this@GlitchSocketObject.ws = null
                    }
                }
            })
        }
    }

    fun disconnect(): Completable = Completable.create {
        if (!isConnected) it.onError(NotYetConnectedException())
        else {
            try {
                ws!!.close(1000, "Disconnected")
            } catch (e: Exception) {
                it.onError(e)
            } finally {
                it.onComplete()
            }
        }
    }

    protected fun sendRaw(message: Publisher<String>): Completable = Observable.fromPublisher(message)
        .concatMapCompletable { m ->
            Completable.create {
                if (!isConnected) it.onError(NotYetConnectedException())
                else {
                    this.ws!!.send(m)
                    it.onComplete()
                }
            }
        }

    fun <E : IEvent> onEvent(eventType: Class<E>): Observable<E> = eventSubject.ofType(eventType)
    fun <E : IEvent> onEvent(eventType: KClass<E>): Observable<E> = onEvent(eventType.java)
    inline fun <reified E : IEvent> onEvent(): Observable<E> = onEvent(E::class)
}