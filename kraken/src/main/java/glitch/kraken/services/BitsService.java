package glitch.kraken.services;

import glitch.GlitchClient;
import glitch.core.api.AbstractService;
import glitch.core.utils.api.BaseURL;
import glitch.core.utils.api.HttpClient;
import glitch.core.utils.api.HttpMethod;
import glitch.core.utils.api.HttpRequest;
import glitch.core.utils.api.Router;
import glitch.kraken.json.Channel;
import glitch.kraken.json.Cheermote;
import glitch.kraken.json.lists.AbstractList;
import glitch.kraken.json.lists.CheermoteList;
import io.reactivex.Observable;

public class BitsService extends AbstractService {
    public BitsService(GlitchClient client, HttpClient httpClient, BaseURL baseURL) {
        super(client, httpClient, baseURL);
    }

    private HttpRequest<CheermoteList> request() {
        return Router.create(HttpMethod.GET, baseURL.endpoint("/bits/actions"), CheermoteList.class)
                .request();
    }

    public Observable<Cheermote> getCheermotes() {
        return request().exchange(httpClient)
                .flatMapObservable(cheers -> Observable.fromIterable(cheers.getData()));
    }

    public Observable<Cheermote> getCheermotes(Channel channel) {
        return getCheermotes(channel.getId());
    }

    public Observable<Cheermote> getCheermotes(Long channelId) {
        return request().queryParam("channel_id", channelId)
                .exchange(httpClient)
                .flatMapObservable(cheers -> Observable.fromIterable(cheers.getData()));
    }
}
