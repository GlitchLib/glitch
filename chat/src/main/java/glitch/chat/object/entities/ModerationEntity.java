package glitch.chat.object.entities;

import glitch.auth.Scope;
import glitch.chat.exceptions.ModerationException;
import glitch.exceptions.http.ScopeIsMissingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

@RequiredArgsConstructor
public class ModerationEntity {

    private static final Set<Integer> commercialDuration = new LinkedHashSet<>(Arrays.asList(30, 60, 90, 120, 150, 180));

    @Getter
    private final ChannelEntity channel;

    private final Mono<ChannelUserEntity> bot;

    ModerationEntity(ChannelEntity channel) {
        this.channel = channel;
        this.bot = channel.getBot();
    }

    /**
     *
     * @param username Twitch User Name
     * @param duration duration in seconds
     * @param reason the reason
     * @return
     */
    public Mono<Void> timeout(String username, Long duration, @Nullable String reason) {
        return bot.flatMap(user -> {
            if (user.isModerator() && hasScope(Scope.CHANNEL_MODERATE)) {
                return channel.sendMessage(Mono.just(String.format("/timeout %s %s", username, duration) + formatReason(reason)));
            } else {
                return Mono.error(noModeratorPermissions());
            }
        });
    }

    public Mono<Void> ban(String username, @Nullable String reason) {
        return bot.flatMap(user -> {
            if (user.isModerator() && hasScope(Scope.CHANNEL_MODERATE)) {
                return channel.sendMessage(Mono.just("/ban "+ username + formatReason(reason)));
            } else {
                return Mono.error(noModeratorPermissions());
            }
        });
    }

    public Mono<Void> unban(String username) {
        return bot.flatMap(user -> {
            if (user.isModerator() && hasScope(Scope.CHANNEL_MODERATE)) {
                return channel.sendMessage(Mono.just("/unban " + username));
            } else {
                return Mono.error(noModeratorPermissions());
            }
        });
    }

    public Mono<Void> slow(Long duration) {
        return bot.flatMap(user -> {
            if (user.isModerator() && hasScope(Scope.CHANNEL_MODERATE)) {
                return channel.sendMessage(Mono.just("/slow " + duration.toString()));
            } else {
                return Mono.error(noModeratorPermissions());
            }
        });
    }

    public Mono<Void> slowOff() {
        return bot.flatMap(user -> {
            if (user.isModerator() && hasScope(Scope.CHANNEL_MODERATE)) {
                return channel.sendMessage(Mono.just("/slowoff"));
            } else {
                return Mono.error(noModeratorPermissions());
            }
        });
    }

    /**
     *
     * @param duration duration in minutes
     * @return
     */
    public Mono<Void> followers(Long duration) {
        return bot.flatMap(user -> {
            if (user.isModerator() && hasScope(Scope.CHANNEL_MODERATE)) {
                if (duration > 129600L) // prevents exceeding limit to the chat (3 months)
                    return Mono.error(new IllegalArgumentException("Duration must be less than 3 months (129600 minutes) - \"" + duration.toString() + "\" minutes"));
                return channel.sendMessage(Mono.just("/followers " + duration.toString()));
            } else {
                return Mono.error(noModeratorPermissions());
            }
        });
    }

    public Mono<Void> followersOff() {
        return bot.flatMap(user -> {
            if (user.isModerator() && hasScope(Scope.CHANNEL_MODERATE)) {
                return channel.sendMessage(Mono.just("/followersoff"));
            } else {
                return Mono.error(noModeratorPermissions());
            }
        });
    }

    public Mono<Void> subscribers() {
        return bot.flatMap(user -> {
            if (user.isModerator() && hasScope(Scope.CHANNEL_MODERATE)) {
                return channel.sendMessage(Mono.just("/subscribers"));
            } else {
                return Mono.error(noModeratorPermissions());
            }
        });
    }

    public Mono<Void> subscribersOff() {
        return bot.flatMap(user -> {
            if (user.isModerator() && hasScope(Scope.CHANNEL_MODERATE)) {
                return channel.sendMessage(Mono.just("/subscribersoff"));
            } else {
                return Mono.error(noModeratorPermissions());
            }
        });
    }

    public Mono<Void> r9k() {
        return bot.flatMap(user -> {
            if (user.isModerator() && hasScope(Scope.CHANNEL_MODERATE)) {
                return channel.sendMessage(Mono.just("/r9kbeta"));
            } else {
                return Mono.error(noModeratorPermissions());
            }
        });
    }

    public Mono<Void> r9kOff() {
        return bot.flatMap(user -> {
            if (user.isModerator() && hasScope(Scope.CHANNEL_MODERATE)) {
                return channel.sendMessage(Mono.just("/r9kbetaoff"));
            } else {
                return Mono.error(noModeratorPermissions());
            }
        });
    }

    public Mono<Void> emoteOnly() {
        return bot.flatMap(user -> {
            if (user.isModerator() && hasScope(Scope.CHANNEL_MODERATE)) {
                return channel.sendMessage(Mono.just("/emoteonly"));
            } else {
                return Mono.error(noModeratorPermissions());
            }
        });
    }

    public Mono<Void> emoteOnlyOff() {
        return bot.flatMap(user -> {
            if (user.isModerator() && hasScope(Scope.CHANNEL_MODERATE)) {
                return channel.sendMessage(Mono.just("/emoteonlyoff"));
            } else {
                return Mono.error(noModeratorPermissions());
            }
        });
    }

    public Mono<Void> clear() {
        return bot.flatMap(user -> {
            if (user.isModerator() && hasScope(Scope.CHANNEL_MODERATE)) {
                return channel.sendMessage(Mono.just("/clear"));
            } else {
                return Mono.error(noModeratorPermissions());
            }
        });
    }

    public Mono<Void> host(String channel) {
        if (hasScope(Scope.CHANNEL_EDITOR)) {
            return this.channel.sendMessage(Mono.just("/host " + channel));
        } else {
            return Mono.error(scopeMissing());
        }
    }

    public Mono<Void> unhost() {
        if (hasScope(Scope.CHANNEL_EDITOR)) {
            return channel.sendMessage(Mono.just("/unhost"));
        } else {
            return Mono.error(scopeMissing());
        }
    }

    public Mono<Void> commercial(Integer duration) {
        if (hasScope(Scope.CHANNEL_EDITOR)) {
            int dur = commercialDuration.stream().min(Comparator.comparingInt(i -> Math.abs(i - duration))).orElse(30);

            return channel.sendMessage(Mono.just("/commercial " + dur));
        } else {
            return Mono.error(scopeMissing());
        }
    }

    public Mono<Void> mod(String username) {
        return bot.flatMap(bot -> {
            if (bot.isBroadcaster()) {
                return channel.sendMessage(Mono.just("/mod " + username));
            } else {
                return Mono.error(new ModerationException("You are not a broadcaster to using this command"));
            }
        });
    }

    public Mono<Void> unmod(String username) {
        return bot.flatMap(bot -> {
            if (bot.isBroadcaster()) {
                return channel.sendMessage(Mono.just("/unmod " + username));
            } else {
                return Mono.error(new ModerationException("You are not a broadcaster to using this command"));
            }
        });
    }

    private String formatReason(@Nullable String reason) {
        return (reason != null) ? " " + reason : "";
    }

    private ModerationException noModeratorPermissions() {
        return new ModerationException("You cannot moderate this channel!");
    }

    private boolean hasScope(Scope scope) {
        return channel.getClient().getConfiguration().getBotCredentials().getScopes().contains(scope);
    }

    private ScopeIsMissingException scopeMissing() {
        return new ScopeIsMissingException(Scope.CHANNEL_EDITOR);
    }

    public Mono<Void> delete(String id) {
        return bot.flatMap(user -> {
            if (user.isModerator() && hasScope(Scope.CHANNEL_MODERATE)) {
                return channel.sendMessage(Mono.just(String.format("/delete %s", id)));
            } else {
                return Mono.error(noModeratorPermissions());
            }
        });
    }
}
