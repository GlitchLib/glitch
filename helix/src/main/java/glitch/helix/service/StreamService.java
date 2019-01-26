package glitch.helix.service;

import glitch.service.AbstractHttpService;
import glitch.helix.GlitchHelix;
import glitch.helix.object.json.User;
import glitch.helix.object.json.Video;
import glitch.helix.service.request.StreamsRequest;

public class StreamService extends AbstractHttpService {
    public StreamService(GlitchHelix helix) {
        super(helix.getClient(), helix.getHttpClient());
    }

    public StreamsRequest getStreams() {
        return new StreamsRequest(http);
    }

    // TODO
    public void getStreamsMetadata() {
        throw new UnsupportedOperationException("Streams Metadata is not supported yet");
    }

    // TODO
    public void createStreamMarker() {
        throw new UnsupportedOperationException("Stream Markers is not supported yet");
    }

    // TODO
    public void getStreamMarker(Video video) {
        throw new UnsupportedOperationException("Stream Markers is not supported yet");
    }

    // TODO
    public void getStreamMarker(User user) {
        throw new UnsupportedOperationException("Stream Markers is not supported yet");
    }
}
