package glitch.helix.service;

import glitch.auth.objects.json.Credential;
import glitch.helix.GlitchHelix;
import glitch.helix.service.request.BitsLeaderboardRequest;
import glitch.service.AbstractHttpService;

public class BitsService extends AbstractHttpService {
    public BitsService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }

    public BitsLeaderboardRequest getLeaderboard(Credential credential) {
        return new BitsLeaderboardRequest(http, credential);
    }
}
