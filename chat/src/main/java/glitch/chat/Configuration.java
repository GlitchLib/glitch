package glitch.chat;


import glitch.auth.objects.json.Credential;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public class Configuration {
    @Getter
    private final Credential botCredentials;

    private final boolean forcingQuery;

    @Setter(AccessLevel.PACKAGE)
    private RateLimiter rateLimits = RateLimiter.global();


    Configuration(Credential botCredentials, boolean forcingQuery) {
        this.botCredentials = botCredentials;
        this.forcingQuery = forcingQuery;

//        getBotGlobalUserState().subscribe(gus -> {
//            if (gus.isVerifiedBot()) {
//                setRateLimits(RateLimiter.verifiedBot());
//            } else if (gus.isKnownBot()) {
//                setRateLimits(RateLimiter.knownBot());
//            }
//        });
    }

    // TODO: Kraken API
//    public Mono<GlobalUserState> getBotGlobalUserState() {
//        return chat.getApi().getGlobalUserState(botCredentials);
//    }

    public Mono<Long> getChatLimits(boolean moderator) {
        return Mono.delay(rateLimits.getChatDuration(moderator));
    }

    public Mono<Long> getChatLimits() {
        return Mono.delay(rateLimits.getChatDuration());
    }

    public Mono<Tuple2<Long, Integer>> getWhisperLimits() {
        return Mono.delay(rateLimits.getWhisperDuration())
                .zipWith(Mono.just(rateLimits.getMaxWhispers()));
    }
}
