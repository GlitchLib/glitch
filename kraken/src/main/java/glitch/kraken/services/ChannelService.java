package glitch.kraken.services;

public interface ChannelService {
//    public ChannelService(GlitchClient client, HttpClient httpClient, BaseURL baseURL) {
//        super(client, httpClient, baseURL);
//    }
//
//    public Single<ChannelVerified> getChannel(Credential credential) {
//        if (!hasRequiredScope(credential, Scope.CHANNEL_READ)) {
//            return Single.error(new ScopeIsMissingException(Scope.CHANNEL_READ));
//        }
//        return Router.create(HttpMethod.GET, baseURL.endpoint("/channel"), ChannelVerified.class)
//                .request()
//                .header("Authorization", authorization("OAuth", credential))
//                .exchange(httpClient);
//    }
//
//    public Single<Channel> getChannel(Long id) {
//        return Router.create(HttpMethod.GET, baseURL.endpoint("/channels/%s"), Channel.class)
//                .request(id)
//                .exchange(httpClient);
//    }
}
