package glitch.kraken.services;

import glitch.api.AbstractHttpService;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.TeamUsers;
import glitch.kraken.object.json.list.Teams;
import glitch.kraken.services.request.TeamsRequest;
import reactor.core.publisher.Mono;

public class TeamService extends AbstractHttpService {
    public TeamService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public TeamsRequest getAllTeams() {
        return new TeamsRequest(http, get("/teams", Teams.class));
    }

    public Mono<TeamUsers> getTeam(String name) {
        return exchange(get(String.format("/teams/%s", name), TeamUsers.class)).toMono();
    }
}
