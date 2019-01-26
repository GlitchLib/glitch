package glitch.chat;

import glitch.auth.objects.json.Credential;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public class Configuration {
    private final Credential botCredentials;

    private final boolean forcingQuery;

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

    public Credential getBotCredentials() {
        return botCredentials;
    }

    Configuration setRateLimits(RateLimiter rateLimits) {
        this.rateLimits = rateLimits;
        return this;
    }

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
