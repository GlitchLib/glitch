package glitch.kraken.services;

import glitch.kraken.json.Channel;
import glitch.kraken.json.Cheermote;
import glitch.kraken.json.lists.CheermoteList;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import javax.annotation.Nullable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public abstract class BitsService {
    public Observable<Cheermote> getCheermotes(@Nullable long channelId) {
        return getCheermoteList(channelId)
                .flatMapObservable(new Function<CheermoteList, ObservableSource<Cheermote>>() {
                    @Override
                    public ObservableSource<Cheermote> apply(CheermoteList cheers) throws Exception {
                        return Observable.fromIterable(cheers.getData());
                    }
                });
    }

    public Observable<Cheermote> getCheermotes(@Nullable Channel channel) {
        return getCheermotes(channel.getId());
    }

    @GET("/bits/actions")
    public Observable<Cheermote> getCheermotes() {
        return getCheermoteList(null)
                .flatMapObservable(new Function<CheermoteList, ObservableSource<Cheermote>>() {
                    @Override
                    public ObservableSource<Cheermote> apply(CheermoteList cheers) throws Exception {
                        return Observable.fromIterable(cheers.getData());
                    }
                });
    }

    @GET("/bits/actions")
    abstract Single<CheermoteList> getCheermoteList(@Nullable @Query("channel_id") Long channelId);
}
