package glitch.kraken.services;

import glitch.api.http.Routes;
import glitch.kraken.GlitchKraken;
import glitch.kraken.object.json.TeamUsers;
import glitch.kraken.services.request.TeamsRequest;
import glitch.service.AbstractHttpService;
import reactor.core.publisher.Mono;

public class TeamService extends AbstractHttpService {
    public TeamService(GlitchKraken rest) {
        super(rest.getClient(), rest.getHttpClient());
    }

    public TeamsRequest getAllTeams() {
        return new TeamsRequest(http);
    }

    public Mono<TeamUsers> getTeam(String name) {
        return exchangeTo(Routes.get("/teams/%s").newRequest(name), TeamUsers.class);
    }
}
