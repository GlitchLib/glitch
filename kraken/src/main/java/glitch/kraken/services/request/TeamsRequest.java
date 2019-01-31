package glitch.kraken.services.request;

import glitch.api.http.HttpClient;
import glitch.api.http.Routes;
import glitch.kraken.object.json.Team;
import glitch.kraken.object.json.collections.Teams;
import glitch.service.AbstractRequest;
import reactor.core.publisher.Mono;

public class TeamsRequest extends AbstractRequest<Team, Teams> {
    private Integer limit;
    private Integer offset;

    public TeamsRequest(HttpClient httpClient) {
        super(httpClient, Routes.get("/teams").newRequest());
    }

    public TeamsRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public TeamsRequest setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public Mono<Teams> get() {

        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        return httpClient.exchangeAs(request, Teams.class);
    }
}
