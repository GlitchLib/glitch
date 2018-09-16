package glitch.chat;

import com.google.common.collect.ImmutableMap;
import glitch.chat.events.RawIRCEvent;
import glitch.chat.events.channel.BanEventImpl;
import glitch.chat.events.channel.ClearChatEventImpl;
import glitch.chat.events.channel.HostEventImpl;
import glitch.chat.events.channel.JoinChannelEventImpl;
import glitch.chat.events.channel.PartChannelEventImpl;
import glitch.chat.events.channel.RaidEventImpl;
import glitch.chat.events.channel.RoomStateChangedEventImpl;
import glitch.chat.events.channel.RoomStateEventImpl;
import glitch.chat.events.channel.TimeoutEventImpl;
import glitch.chat.events.channel.UnhostEventImpl;
import glitch.chat.events.channel.UserNoticeEventImpl;
import glitch.chat.events.chat.ActionMessageEventImpl;
import glitch.chat.events.chat.BitsMessageEventImpl;
import glitch.chat.events.chat.MessageEventImpl;
import glitch.chat.events.chat.NewChatterEventImpl;
import glitch.chat.events.chat.OrdinalMessageEventImpl;
import glitch.chat.events.chat.PrivateMessageEventImpl;
import glitch.chat.events.chat.RitualNoticeEventImpl;
import glitch.chat.events.chat.SubscribeEventImpl;
import glitch.chat.events.chat.SubscribeGiftEventImpl;
import glitch.chat.events.states.GlobalUserStateEventImpl;
import glitch.chat.events.states.NoticeEventImpl;
import glitch.chat.events.states.UserStateEvent;
import glitch.chat.events.states.UserStateEventImpl;
import glitch.core.api.json.Badge;
import glitch.core.api.json.BadgeImpl;
import glitch.core.api.json.enums.SubscriptionType;
import glitch.socket.events.Event;
import glitch.socket.events.actions.PingEventImpl;
import glitch.socket.events.actions.PongEventImpl;
import io.reactivex.functions.Consumer;
import java.awt.Color;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("unchecked")
@RequiredArgsConstructor
class IRCMessageConsumer<E extends Event<GlitchChat>> implements Consumer<RawIRCEvent> {
    private final GlitchChat client;

    @Override
    public void accept(RawIRCEvent rawEvent) {
        E event = null;

        Optional<String> channel = rawEvent.getTrailing()
                .stream()
                .filter(s -> s.startsWith("#"))
                .map(s -> s.substring(1))
                .findFirst();
        boolean hasAction = rawEvent.getMiddle().contains("ACTION");
        ImmutableMap<String, String> tags = rawEvent.getTags();
        String user = rawEvent.getPrefix().getUser();

        String message = String.join(" ", rawEvent.getMiddle()).replace("\u0001", "").replace("ACTION ", "");

        switch (rawEvent.getCommand()) {
            case JOIN:
                event = (E) JoinChannelEventImpl.of(
                        channel.get(),
                        rawEvent.getCreatedAt(),
                        UUID.randomUUID().toString(),
                        client,
                        user
                );
                break;
            case PART:
                event = (E) PartChannelEventImpl.of(
                        channel.get(),
                        rawEvent.getCreatedAt(),
                        UUID.randomUUID().toString(),
                        client,
                        user
                );
                break;
            case PRIV_MSG:
                client.getPublisher().onNext(MessageEventImpl.of(
                        tags,
                        message,
                        hasAction,
                        channel.get(),
                        rawEvent.getCreatedAt(),
                        UUID.randomUUID().toString(),
                        client,
                        user
                ));
                event = (E) fetchMessageEvent(tags, user, channel, message, hasAction, rawEvent.getCreatedAt());
                break;
            case WHISPER:
                event = (E) PrivateMessageEventImpl.of(message, user, rawEvent.getCreatedAt(), UUID.randomUUID().toString(), client);
                break;
            case PONG:
                event = (E) PongEventImpl.of(
                        rawEvent.getCreatedAt(),
                        UUID.randomUUID().toString(),
                        client
                );
                break;
            case PING:
                event = (E) PingEventImpl.of(
                        rawEvent.getCreatedAt(),
                        UUID.randomUUID().toString(),
                        client
                );
                break;
            case USER_NOTICE:
                client.getPublisher().onNext(
                        UserNoticeEventImpl.of(
                                tags,
                                message,
                                channel.get(),
                                rawEvent.getCreatedAt(),
                                UUID.randomUUID().toString(),
                                client
                        )
                );
                event = (E) fetchSubRaidRitualEvent(tags, channel, message, rawEvent.getCreatedAt());
                break;
            case NOTICE:
                event = (E) NoticeEventImpl.of(
                        tags.get("msg-id").toString(),
                        message,
                        channel.get(),
                        rawEvent.getCreatedAt(),
                        UUID.randomUUID().toString(),
                        client
                );
                break;
            case HOST_TARGET:
                boolean host = message == null || message.equals("");
                String hostedChannel = rawEvent.getTrailing().get(1);
                long viewers = Long.parseLong(rawEvent.getTrailing().get(2).substring(1).replace("]", ""));
                if (host) {
                    event = (E) HostEventImpl.of(
                            viewers,
                            hostedChannel,
                            channel.get(),
                            rawEvent.getCreatedAt(),
                            UUID.randomUUID().toString(),
                            client
                    );
                } else {
                    event = (E) UnhostEventImpl.of(
                            channel.get(),
                            rawEvent.getCreatedAt(),
                            UUID.randomUUID().toString(),
                            client
                    );
                }
                break;
            case CLEAR_CHAT:
                if (message != null && !message.equals("")) {
                    String reason = tags.getOrDefault("ban-reason", null);

                    if (tags.containsKey("ban-duration")) {
                        event = (E) TimeoutEventImpl.of(
                                Long.parseLong(tags.get("ban-duration")),
                                reason,
                                channel.get(),
                                rawEvent.getCreatedAt(),
                                UUID.randomUUID().toString(),
                                client,
                                message.replace(" ", "")
                        );
                    } else {
                        event = (E) BanEventImpl.of(
                                reason,
                                channel.get(),
                                rawEvent.getCreatedAt(),
                                UUID.randomUUID().toString(),
                                client,
                                message.replace(" ", "")
                        );
                    }
                } else {
                    event = (E) ClearChatEventImpl.of(
                            channel.get(),
                            rawEvent.getCreatedAt(),
                            UUID.randomUUID().toString(),
                            client
                    );
                }
                break;
            case ROOM_STATE:
                if (tags.size() == 1) {
                    String key = (String) tags.keySet().toArray()[0];
                    String value = tags.get(key);
                    event = (E) RoomStateChangedEventImpl.of(
                            key,
                            value,
                            channel.get(),
                            rawEvent.getCreatedAt(),
                            UUID.randomUUID().toString(),
                            client
                    );
                } else {
                    Locale broadcastLang = (tags.get("broadcast-lang") != null && !tags.get("broadcast-lang").equals("")) ? Locale.forLanguageTag(tags.get("broadcast-lang")) : null;
                    boolean r9k = tags.get("r9k").equals("1");
                    long slow = Long.parseLong(tags.get("slow"));
                    boolean subsOnly = tags.get("subs-only").equals("1");
                    event = (E) RoomStateEventImpl.of(
                            broadcastLang,
                            r9k, slow, subsOnly, channel.get(),
                            rawEvent.getCreatedAt(),
                            UUID.randomUUID().toString(),
                            client
                    );
                }
                break;
            case GLOBAL_USER_STATE:
                Color color = getColor(tags.get("color"));
                String displayName = tags.get("display_name");
                boolean turbo = tags.get("mod").equals("1");
                String userType = tags.get("user_type");
                long userId = Long.parseLong(tags.get("user_id"));
                event = (E) GlobalUserStateEventImpl.of(
                        color,
                        displayName,
                        turbo,
                        userId,
                        userType,
                        rawEvent.getCreatedAt(),
                        UUID.randomUUID().toString(),
                        client
                );
                break;
            case USER_STATE:
                event = (E) getUserState(tags, channel, rawEvent.getCreatedAt());
                break;
            default:
                break;
        }

        if (!Objects.isNull(event)) {
            client.getPublisher().onNext(event);
        }
    }


    private UserStateEvent getUserState(ImmutableMap<String, String> tags, Optional<String> channel, Instant created) {
        Color color = getColor(tags.get("color"));
        String displayName = tags.get("display_name");
        boolean mod = tags.get("mod").equals("1");
        boolean subscriber = tags.get("mod").equals("1");
        boolean turbo = tags.get("mod").equals("1");
        String userType = tags.get("user_type");
        return UserStateEventImpl.of(color, mod, subscriber, turbo, displayName, userType, channel.get(), created, UUID.randomUUID().toString(), client);
    }

    private <E extends Event<GlitchChat>> E fetchMessageEvent(ImmutableMap<String, String> tags, String user, Optional<String> channel, String message, boolean isAction, Instant created) {
        List<Badge> badges = Arrays.stream(tags.get("badges").split(","))
                .map(badge -> BadgeImpl.of(Integer.parseInt(badge.split("/")[1]), badge.split("/")[0]))
                .collect(Collectors.toList());
        int bits = tags.containsKey("bits") ? Integer.parseInt(tags.get("bits")) : 0;
        Color color = getColor(tags.get("color"));
        String displayName = tags.get("display-name");
        // TODO: Emotes
        boolean mod = tags.containsKey("mod") && tags.get("mod").equals("1");
        Long channelId = Long.parseLong(tags.get("room-id"));
        boolean subscriber = tags.containsKey("subscriber") && tags.get("subscriber").equals("1");
        boolean turbo = tags.containsKey("turbo") && tags.get("turbo").equals("1");
        Long userId = Long.parseLong(tags.get("user-id"));
        String userType = tags.get("user-type");
        if (bits > 0) {
            return (E) BitsMessageEventImpl.of(bits, badges, color, mod, subscriber, turbo, message, channel.get(),
                    created, UUID.randomUUID().toString(), client, user);
        } else if (isAction) {
            return (E) ActionMessageEventImpl.of(badges, color, mod, subscriber, turbo, message, channel.get(),
                    created, UUID.randomUUID().toString(), client, user);
        } else {
            return (E) OrdinalMessageEventImpl.of(badges, color, mod, subscriber, turbo, message, channel.get(),
                    created, UUID.randomUUID().toString(), client, user);
        }
    }

    private Event fetchSubRaidRitualEvent(ImmutableMap<String, String> tags, Optional<String> channel, String message, Instant created) {
        List<Badge> badges = Arrays.stream(tags.get("badges").split(","))
                .map(badge -> BadgeImpl.of(Integer.parseInt(badge.split("/")[1]), badge.split("/")[0]))
                .collect(Collectors.toList());
        Color color = getColor(tags.get("color"));
        String user = tags.get("login");
        boolean mod = tags.containsKey("mod") && tags.get("mod").equals("1");
        String messageId = tags.get("msg-id");
        Long channelId = Long.parseLong(tags.get("room-id"));
        boolean subscriber = tags.containsKey("subscriber") && tags.get("subscriber").equals("1");
        boolean turbo = tags.containsKey("turbo") && tags.get("turbo").equals("1");
        String userType = tags.get("user-type");

        Event event = null;

        switch (messageId) {
            case "raid":
                long viewcount = Long.parseLong(tags.get("msg-param-viewerCount"));
                event = RaidEventImpl.of(viewcount, badges, color, mod, subscriber, turbo, message, channel.get(), created, UUID.randomUUID().toString(), client, user);
                break;
            case "ritual":
                if (tags.containsKey("msg-param-ritual-name") && tags.get("msg-param-ritual-name").equals("new_chatter")) {
                    event = NewChatterEventImpl.of(badges, color, mod, subscriber, turbo, message, channel.get(), created, UUID.randomUUID().toString(), client, user);
                } else {
                    event = RitualNoticeEventImpl.of(badges, color, mod, subscriber, turbo, message, channel.get(), created, UUID.randomUUID().toString(), client, user);
                }
                break;
            case "sub":
            case "resub":
            case "subgift":
                SubscriptionType subType = SubscriptionType.from(tags.get("msg-param-sub-plan"));
                int months = (tags.containsKey("msg-param-months") && !tags.get("msg-param-months").equals("0")) ? Integer.parseInt(tags.get("msg-param-months")) : 1;
                String gifter = (tags.containsKey("msg-param-recipient-user-name") && !tags.get("msg-param-recipient-user-name").equals("")) ? tags.get("msg-param-recipient-user-name") : null;
                event = SubscribeEventImpl.of(months, gifter, subType, badges, color, mod, true, turbo, message, channel.get(), created, UUID.randomUUID().toString(), client, user);
                break;
            case "submysterygift":
                SubscriptionType massSubType = SubscriptionType.from(tags.get("msg-param-sub-plan"));
                int giftCount = (tags.containsKey("msg-param-mass-gift-count")) ? Integer.parseInt(tags.get("msg-param-mass-gift-count")) : 0;
                int totalGiftCount = (tags.containsKey("msg-param-sender-count")) ? Integer.parseInt(tags.get("msg-param-sender-count")) : 0;
                event = SubscribeGiftEventImpl.of(massSubType, giftCount, totalGiftCount, channel.get(), created, UUID.randomUUID().toString(), client, user);
                break;
        }
        return event;
    }

    @Nullable
    private Color getColor(String color) {
        if (color == null && !color.equals("") && color.startsWith("#")) {
            return Color.decode(color);
        }

        return null;
    }
}
