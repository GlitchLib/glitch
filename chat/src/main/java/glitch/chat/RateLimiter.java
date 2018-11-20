package glitch.chat;

import glitch.kraken.object.json.GlobalUserState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RateLimiter {

    /**
     * Currently channel limits for each messages sended.
     */
    private final Duration chatDuration;

    /**
     * Whisper limits for each messages sended.
     */
    private final Duration whisperDuration;

    /**
     * Maximum accounts per day.
     *
     * This limits, can blocks sending messages to user, more than current value
     */
    private final int maxWhispers;

    public static RateLimiter fromUserState(GlobalUserState gus) {
        if (gus.isVerifiedBot()) {
            return verifiedBot();
        } else if (gus.isKnownBot()) {
            return knownBot();
        } else {
            return global();
        }
    }

    /**
     * Getting chat duration with highest rate limits if moderator state is {@code true}
     * @param moderator getting highest limits if this one is {@code true}
     * @return getting duration of highest rate limit.
     */
    Duration getChatDuration(boolean moderator) {
        if (moderator) {
            Duration chatDuration = Duration.ofMillis(30000 / 100);
            if (this.chatDuration.toMillis() > chatDuration.toMillis()) {
                chatDuration = this.chatDuration;
            }
            return chatDuration;
        } else {
            return chatDuration;
        }
    }

    /**
     * Brought you by Global Rate limits
     */
    static RateLimiter global() {
        return new RateLimiter(Duration.ofMillis(30000 / 20), Duration.ofMillis(60000/100), 40);
    }

    /**
     * Brought you by Know Bot Rate limits
     */
    static RateLimiter knownBot() {
        return new RateLimiter(Duration.ofMillis(30000 / 50), Duration.ofMillis(60000/200), 500);
    }


    /**
     * Brought you by Verified Bot Rate limits
     */
    static RateLimiter verifiedBot() {
        return new RateLimiter(Duration.ofMillis(30000 / 7500), Duration.ofMillis(60000/1200), 100000);
    }
}
