package glitch.helix.service.request;

import glitch.api.http.HttpClient;
import glitch.api.http.HttpRequest;
import glitch.api.http.HttpResponse;
import glitch.helix.object.json.Video;
import glitch.helix.object.json.list.Videos;
import glitch.service.AbstractRestService;
import lombok.Setter;
import lombok.experimental.Accessors;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Setter
@Accessors(chain = true, fluent = true)
public class VideoRequest extends AbstractRestService.AbstractRequest<Videos, Video> {
    public VideoRequest(HttpClient httpClient, HttpRequest<Videos> request) {
        super(httpClient, request);
    }

    @Override
    protected HttpResponse<Videos> exchange() {
        return null;
    }

    @Override
    public Mono<Videos> get() {
        return null;
    }

    @Override
    public Flux<Video> getIterable() {
        return null;
    }
}
