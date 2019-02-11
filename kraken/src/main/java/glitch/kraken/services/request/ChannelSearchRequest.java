package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.kraken.object.json.Channel;
import glitch.kraken.object.json.collections.Channels;
import glitch.service.AbstractRequest;
import reactor.core.publisher.Mono;

public class ChannelSearchRequest extends AbstractRequest<Channel, Channels> {
    private Integer offset;
    private Integer limit;

    public ChannelSearchRequest(HttpClient http, String query) {
        super(http, Routes.get("/search/channels").newRequest().queryParam("query", query));
    }

    public ChannelSearchRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public ChannelSearchRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public Mono<Channels> get() {
        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        return httpClient.exchangeAs(request, Channels.class);
    }
}
