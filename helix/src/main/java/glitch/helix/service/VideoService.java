package glitch.helix.service;

import glitch.api.http.Routes;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.helix.GlitchHelix;
import glitch.helix.object.json.Game;
import glitch.helix.object.json.User;
import glitch.helix.object.json.Video;
import glitch.helix.object.json.Videos;
import glitch.helix.service.request.VideoRequest;
import glitch.service.AbstractHttpService;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;
import reactor.core.publisher.Flux;

public class VideoService extends AbstractHttpService {
    public VideoService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }

    private static MultiValuedMap<String, Object> mapIds(List<Long> ids) {
        MultiValuedMap<String, Object> map = MultiMapUtils.newListValuedHashMap();

        ids.forEach(id -> map.put("id", id));

        return map;
    }

    public Flux<Video> getVideos(Long... id) {
        return http.exchangeAs(Routes.get("/videos").newRequest()
                .queryParams(mapIds(Arrays.asList(Arrays.copyOf(id, 100)))), Videos.class)
                .flatMapIterable(OrdinalList::getData);
    }

    public VideoRequest getVideos(User user) {
        return new VideoRequest(http, user);
    }

    public VideoRequest getVideos(Game game) {
        return new VideoRequest(http, game);
    }
}
