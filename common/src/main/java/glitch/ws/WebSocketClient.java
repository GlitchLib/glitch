package glitch.ws;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;
import glitch.events.EventManager;
import io.reactivex.Single;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.atomic.AtomicReference;

public abstract class WebSocketClient {
    private final String url;
    final EventManager eventBus;
    private final AtomicReference<WebSocket> ws = new AtomicReference<>();

    public WebSocketClient(String url, EventManager eventBus) throws MalformedURLException {
        if (url.matches("^(ws|wss)://(.+)")) {
            this.url = url;
        } else throw new MalformedURLException("The URL must be a WebSocket prefix path (ws:// or wss://)");

        this.eventBus = eventBus;
    }

    protected Single<Void> send(String message) {
        return Single.create(sub -> {
            if (ws.get() == null) throw new RuntimeException("Socket is not connected!!!");
            ws.get().sendText(message);
        });
    }

    protected void sendAsync(String message) {
        send(message).blockingGet();
    }

    public Single<WebSocket> connect() {
        return Single.fromPublisher(publisher -> {
            if (this.ws.get() != null) publisher.onError(new RuntimeException("Socket is already connected!!!"));
            try {
                WebSocket ws = new WebSocketFactory().createSocket(url);
                ws.addListener(new WebSocketListener<>(this));
                this.ws.set(ws);
                publisher.onNext(ws);
            } catch (IOException e) {
                publisher.onError(e);
            }
        });
    }

    public void connectAsync() {
        connect().subscribe(ws -> {
            if (this.ws.get() == null) {
                this.ws.set(ws);
            }
        }).dispose();
    }

    public Single<Void> close(boolean retry) {
        return Single.fromPublisher(publisher -> {
            WebSocket ws = this.ws.get();
            if (ws != null) {
                ws.sendClose(1000, (retry) ? "Reconnecting..." : "Disconnected!");
                this.ws.set(null);
                if (retry) {
                    connect().doOnError(publisher::onError).subscribe(socket -> publisher.onComplete()).dispose();
                } else publisher.onComplete();
            } else publisher.onError(new RuntimeException("Socket is not connected!!!"));
        });
    }

    public void closeAsync(boolean retry) {
        close(retry).subscribe().dispose();
    }
}
