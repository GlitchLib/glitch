package glitch.test.api.http;

import glitch.api.http.HttpClient;
import glitch.api.http.HttpRequest;
import glitch.api.http.Routes;
import java.io.IOException;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class HttpClientTest {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClientTest.class);
    private static final MockWebServer webserver = new MockWebServer();
    private HttpClient httpClient = HttpClient.builder()
            .withBaseUrl("http://localhost:8080/endpoint")
            .addHeader("Client-ID", "0123456789")
            .build();

    @BeforeClass
    public static void setUp() {
        webserver.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                if (request.getPath().contains("/endpoint/test1")) {
                    return new MockResponse()
                            .setBody("{\"primary\": \"Tested Primary Endpoint\"}")
                            .setHeader("Client-ID", request.getHeader("Client-ID"))
                            .setResponseCode(200);
                } else if (request.getPath().contains("/endpoint/test2")) {
                    return new MockResponse()
                            .setHeader("Client-ID", request.getHeader("Client-ID"))
                            .setBody("{\"primary\": \"Tested Primary Endpoint\", \"secondary\": false, \"tertiary\": 104, \"quaternary\": null}")
                            .setResponseCode(200);
                } else {
                    return new MockResponse().setResponseCode(404)
                            .setHeader("Client-ID", request.getHeader("Client-ID"))
                            .setBody("{\"status\":404,\"message\":\"Endpoint Not Found\",\"error\":\"Not Found\"}");
                }
            }
        });

        try {
            webserver.start(8080);
        } catch (IOException e) {
            LOG.error("Cannot start web server!", e);
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            webserver.close();
        } catch (IOException e) {
            LOG.error("Cannot stopping web server!", e);
        }
    }

    @Test
    public void stringResponse() {
        HttpRequest request = Routes.get("/test1").newRequest();

        StepVerifier.create(httpClient.exchange(request))
                .expectSubscription()
                .expectNextMatches(response ->
                        (response != null && response.isSuccessful()) && response.getStatus().getCode() == 200 &&
                                response.getHeader("Client-ID", 0).equals("0123456789") &&
                                response.getBodyString().equals("{\"primary\": \"Tested Primary Endpoint\"}"))
                .expectComplete()
                .verify();
    }

    @Test
    public void jsonResponse() {
        HttpRequest request = Routes.get("/test2").newRequest();

        StepVerifier.create(httpClient.exchangeAs(request, TestingResponse.class))
                .expectSubscription()
                .expectNextMatches(body -> {
                    assertEquals(body.getPrimary(), "Tested Primary Endpoint");
                    assertFalse(body.isSecondary());
                    assertEquals(body.getTertiary(), 104L);
                    assertNull(body.getQuaternary());
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    public void testExceptionResponse() {
        HttpRequest request = Routes.get("/test3").newRequest();

        StepVerifier.create(httpClient.exchange(request))
                .expectSubscription()
                .expectNextMatches(response -> response != null && response.isError() && response.getStatus().getCode() == 404)
                .expectComplete()
                .verify();
    }
}
