package glitch.helix.service;

import glitch.api.AbstractHttpService;
import glitch.api.http.HttpRequest;
import glitch.helix.GlitchHelix;
import glitch.helix.object.json.Game;
import glitch.helix.object.json.User;
import glitch.helix.object.json.list.Videos;
import glitch.helix.service.request.VideoRequest;

import java.util.Arrays;

public class VideoService extends AbstractHttpService {
    public VideoService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }

    public VideoRequest getVideos(Long... id) {
        HttpRequest<Videos> request = get("/videos", Videos.class);

        Arrays.asList(Arrays.copyOf(id, 100))
                .forEach(i -> request.queryParam("id", i));

        return new VideoRequest(http, request);
    }

    public VideoRequest getVideos(User user) {
        return new VideoRequest(http, get("/videos", Videos.class).queryParam("user_id", user.getId()));
    }

    public VideoRequest getVideos(Game game) {
        return new VideoRequest(http, get("/videos", Videos.class).queryParam("user_id", game.getId()));
    }


}
