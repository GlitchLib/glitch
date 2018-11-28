package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.api.http.HttpRequest;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.Team;
import glitch.kraken.object.json.TeamUsers;
import glitch.kraken.object.json.list.Teams;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TeamService extends AbstractHttpService {
    public TeamService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public Flux<Team> getAllTeams(Integer limit, Integer offset) {
        HttpRequest<Teams> request = get("/teams", Teams.class);

        if (limit != null && limit > 0 && limit <= 100) {
            request.queryParam("limit", limit);
        }

        if (offset != null && offset >= 0) {
            request.queryParam("offset", offset);
        }

        return exchange(request).toFlux(OrdinalList::getData);
    }

    public Mono<TeamUsers> getTeam(String name) {
        return exchange(get(String.format("/teams/%s", name), TeamUsers.class)).toMono();
    }
}
