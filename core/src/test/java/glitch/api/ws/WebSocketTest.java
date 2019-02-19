package glitch.api.ws;

import java.util.stream.StreamSupport;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import reactor.test.StepVerifier;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class WebSocketTest {
    private static WebSocketClient ws = new WebSocketClient();

    @BeforeClass
    public static void setUp() {
        ws.login().subscribe();
    }

    @AfterClass
    public static void tearDown() {
        ws.logout();
    }

    @Test
    public void echoTest() {
        ws.send("{\"hello\":\"world\"}").subscribe(ignore ->
                StepVerifier.create(ws.onEvent(MessageEvent.class))
                        .expectFusion()
                        .expectNextMatches(event -> event.getData().getAsJsonObject().getAsJsonPrimitive("hello").getAsString().equals("world"))
                        .expectComplete()
                        .verify()
        );
    }

    @Test
    public void arrayTest() {
        ws.send("[{\"hello\":\"world\"},{\"pick\":1}]").subscribe(ignore ->
                StepVerifier.create(ws.onEvent(MessageEvent.class))
                        .expectFusion()
                        .expectNextMatches(event -> StreamSupport.stream(event.getData().getAsJsonArray().spliterator(), true)
                                .anyMatch(e -> e.getAsJsonObject().getAsJsonPrimitive("hello")
                                        .getAsString().equals("world")))
                        .expectComplete()
                        .verify()
        );
    }
}