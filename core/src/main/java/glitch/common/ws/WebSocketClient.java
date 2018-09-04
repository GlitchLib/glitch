package glitch.common.ws;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;
import glitch.common.events.EventManager;
import io.reactivex.Single;
import java.net.MalformedURLException;
import java.util.concurrent.atomic.AtomicReference;

public abstract class WebSocketClient {
    private final String url;
    protected final EventManager manager;
    private final AtomicReference<WebSocket> ws = new AtomicReference<>();

    public WebSocketClient(String url, EventManager manager) throws MalformedURLException {
        if (url.matches("^(ws|wss)://(.+)")) {
            this.url = url;
        } else throw new MalformedURLException("The URL must be a WebSocket prefix path (ws:// or wss://)");

        this.manager = manager;
    }

    protected Single<Void> send(String message) {
        return Single.fromPublisher(pub -> {
            try {
                sendAsync(message);
                pub.onComplete();
            } catch (Exception e) {
                pub.onError(e);
            }
        });
    }

    protected void sendAsync(String message) throws Exception {
        if (ws.get() == null) throw new RuntimeException("Socket is not connected!!!");
        ws.get().sendText(message);
    }

    public Single<Void> connect() {
        return Single.fromPublisher(pub -> {
            try {
                connectAsync();
                pub.onComplete();
            } catch (Exception e) {
                pub.onError(e);
            }
        });
    }

    public void connectAsync() throws Exception {
        if (this.ws.get() != null) throw new RuntimeException("Socket is already connected!!!");
        WebSocket ws = new WebSocketFactory().createSocket(url);
        ws.addListener(new WebSocketListener<>(this));
        this.ws.set(ws);
    }

    public Single<Void> close(boolean retry) {
        return Single.fromPublisher(pub -> {
            try {
                closeAsync(retry);
                pub.onComplete();
            } catch (Exception e) {
                pub.onError(e);
            }
        });
    }

    public void closeAsync(boolean retry) throws Exception {
        WebSocket ws = this.ws.get();
        if (ws != null) {
            ws.sendClose(1000, (retry) ? "Reconnecting..." : "Disconnected!");
            this.ws.set(null);
            if (retry) {
                connectAsync();
            }
        } else throw new RuntimeException("Socket is not connected!!!");
    }
}
