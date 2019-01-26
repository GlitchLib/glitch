package glitch.api.http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

/**
 * HTTP Response {@link HttpClient#exchange(HttpRequest) Exchanged} via {@link HttpRequest}
 * The last one must be composed into implemented {@link Iterable} interface.
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class HttpResponse {
    private final Status status;
    private final MultiValuedMap<String, String> headers;
    @Nullable
    private final byte[] body;
    private final HttpRequest request;
    private final Gson gson;

    public HttpResponse(Status status, MultiValuedMap<String, String> headers, @Nullable byte[] body, HttpRequest request, Gson gson) {
        this.status = status;
        this.headers = headers;
        this.body = body;
        this.request = request;
        this.gson = gson;
    }

    public Status getStatus() {
        return status;
    }

    public MultiValuedMap<String, String> getHeaders() {
        return headers;
    }

    @Nullable
    public byte[] getBody() {
        return body;
    }

    @Nullable
    public String getBodyString() { return (body != null) ? new String(body, Charset.forName("UTF-8")) : null; }

    @Nullable
    public <T> T getBodyAs(Class<T> type) throws JsonSyntaxException {
        if (body == null || type.isAssignableFrom(void.class) || type.isAssignableFrom(Void.class)) {
            return null;
        } else {
            return gson.fromJson(getBodyString(), type);
        }
    }

    public HttpRequest getRequest() { return request; }

    /**
     * Getting values of Key Header
     * @param key Header Key
     * @return {@link java.util.List} of values
     */
    public List<String> getHeader(String key) { return new ArrayList<>(headers.get(key)); }

    /**
     * Getting value of Key Header and value index
     * @param key Header Key
     * @param index Header Value index
     * @return Header Value
     */
    public String getHeader(String key, int index) {
        return getHeader(key).get(index);
    }

    /**
     * Status codes are in the HTTP Error Code range
     * @return error status
     */
    public boolean isError() {
        return status.code >= 400 && status.code < 600;
    }

    /**
     * Status codes are in the HTTP Code range
     * @return successful executed response
     */
    public boolean isSuccessful() {
        return status.code >= 200 && status.code < 400;
    }

    /**
     * Response Status POJO
     */
    public static class Status {
        private final int code;
        private final String message;

        public Status(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Status)) return false;
            Status status = (Status) o;
            return getCode() == status.getCode() &&
                    getMessage().equals(status.getMessage());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getCode(), getMessage());
        }

        @Override
        public String toString() {
            return "Status{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
