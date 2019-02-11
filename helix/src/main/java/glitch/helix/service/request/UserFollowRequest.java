package glitch.helix.service.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.helix.object.json.Follow;
import glitch.helix.object.json.Follows;
import glitch.service.AbstractRequest;
import javax.annotation.Nullable;
import reactor.core.publisher.Mono;

public class UserFollowRequest extends AbstractRequest<Follow, Follows> {
    @Nullable
    private final Long fromId;
    @Nullable
    private final Long toId;
    private String after;
    private Integer first;

    public UserFollowRequest(HttpClient httpClient, Long fromId, Long toId) {
        super(httpClient, Routes.get("/users/follows").newRequest());
        this.fromId = fromId;
        this.toId = toId;
    }

    public UserFollowRequest setAfter(String after) {
        this.after = after;
        return this;
    }

    public UserFollowRequest setFirst(Integer first) {
        this.first = first;
        return this;
    }

    @Override
    public Mono<Follows> get() {
        if (after != null) {
            request.queryParam("after", after);
        }

        if (first != null && first > 0 && first <= 100) {
            request.queryParam("first", first);
        }

        if (fromId != null) {
            request.queryParam("from_id", fromId);
        }

        if (toId != null) {
            request.queryParam("to_id", toId);
        }

        return httpClient.exchangeAs(request, Follows.class);
    }
}
