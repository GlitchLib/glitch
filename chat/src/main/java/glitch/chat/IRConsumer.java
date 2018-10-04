package glitch.chat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import glitch.chat.events.*;
import glitch.chat.irc.EmoteIndex;
import glitch.core.api.json.Badge;
import glitch.core.api.json.enums.SubscriptionType;
import glitch.socket.events.PingEvent;
import glitch.socket.events.PingEventImpl;
import glitch.socket.events.PongEvent;
import glitch.socket.events.PongEventImpl;
import java.awt.Color;
import java.time.Instant;
import java.util.Locale;
import javax.annotation.Nullable;

public class IRConsumer {

    public static void composeFrom(RawIRCEvent event) {
        switch (event.getCommand()) {
            case JOIN:
                event.getClient().getDispatcher().onNext(joinEvent(event));
                break;
            case PART:
                event.getClient().getDispatcher().onNext(partEvent(event));
                break;
            case PRIV_MSG:
                handleMessage(event);
                break;
            case WHISPER:
                event.getClient().getDispatcher().onNext(privateMessage(event));
                break;
            case PING:
                event.getClient().getDispatcher().onNext(pingEvent(event));
                break;
            case PONG:
                event.getClient().getDispatcher().onNext(pongEvent(event));
                break;
            case USER_NOTICE:
                handleUserNotice(event);
                break;
            case NOTICE:
                event.getClient().getDispatcher().onNext(noticeEvent(event));
                break;
            case HOST_TARGET:
                handleHost(event);
                break;
            case CLEAR_CHAT:
                handleClearChat(event);
                break;
            case ROOM_STATE:
                handleRoomState(event);
                break;
            case GLOBAL_USER_STATE:
                event.getClient().getDispatcher().onNext(globalUserState(event));
                break;
            case USER_STATE:
                event.getClient().getDispatcher().onNext(userState(event));
                break;
        }
    }

    private static void handleMessage(RawIRCEvent event) {
        ImmutableSet<Badge> badges = event.getTags().getBadges();
        int bits = Integer.parseInt(event.getTags().getOrDefault("bits", "0"));
        Color color = event.getTags().getColor();
        String displayName = event.getTags().get("display-name");
        ImmutableList<EmoteIndex> emotes = event.getTags().getEmotes();
        String message = parseMessage(event);
        boolean mod = event.getTags().getBoolean("mod");
        long channelId = event.getTags().getLong("room-id");
        Instant timestamp = event.getTags().getSentTimestamp();
        long userId = event.getTags().getLong("user-id");
        String userName = getUser(event);
        String channelName = getChannel(event);
        boolean actionMessage = hasAction(event);

        if (bits > 0) {
            event.getClient().getDispatcher().onNext(BitsMessageEventImpl
                    .of(bits, channelId, message, actionMessage, emotes, mod, badges,
                            color, displayName, userId, timestamp, event.getClient(),
                            channelName, userName));
        } else {
            event.getClient().getDispatcher().onNext(MessageEventImpl
                    .of(channelId, message, actionMessage, emotes, mod, badges,
                            color, displayName, userId, timestamp, event.getClient(),
                            channelName, userName));
        }
    }

    private static void handleRoomState(RawIRCEvent event) {
        String channel = getChannel(event);
        if (event.getTags().size() > 1) {
            String key = event.getTags().keySet().stream().findFirst().get();
            String value = event.getTags().get(key);
            event.getClient().getDispatcher().onNext(RoomStateChangedEventImpl.of(key, value, channel, event.getCreatedAt(), event.getClient()));
        } else {
            event.getClient().getDispatcher().onNext(RoomStateEventImpl.of(
                    event.getTags().getBroadcasterLanguage(),
                    event.getTags().getBoolean("emote-only"),
                    event.getTags().getLong("followers-only"),
                    event.getTags().getBoolean("r9k"),
                    event.getTags().getInteger("slow"),
                    event.getTags().getBoolean("subs-only"),
                    channel,
                    event.getCreatedAt(),
                    event.getClient()
            ));
        }
    }

    private static void handleClearChat(RawIRCEvent event) {
        String banReason = event.getTags().getOrDefault("ban-reason", null);
        String channel = getChannel(event);
        if (event.getTrailing() != null && !event.getTrailing().equals("")) {
            String user = event.getTrailing();
            if (event.getTags().containsKey("ban-duration")) {
                event.getClient().getDispatcher().onNext(TimeoutEventImpl.of(event.getTags().getLong("ban-duration"), banReason, channel, event.getCreatedAt(), event.getClient(), user));
            } else {
                event.getClient().getDispatcher().onNext(BanEventImpl.of(banReason, channel, event.getCreatedAt(), event.getClient(), user));
            }
        } else {
            event.getClient().getDispatcher().onNext(ClearChatEventImpl.of(channel, event.getCreatedAt(), event.getClient()));
        }
    }

    private static void handleHost(RawIRCEvent event) {
        String message = parseMessage(event);
        boolean host = !message.startsWith("- ");
        String hostedChannel = event.getMiddle().get(1);
        ChannelEvent channelEvent = channelEvent(event);
        long viewers = Long.parseLong(((host) ? event.getMiddle().get(2) : message.substring(3)).replaceAll("[\\[\\]]", ""));
        if (host) {
            event.getClient().getDispatcher().onNext(
                    HostEventImpl.builder()
                            .from(channelEvent)
                            .viewers(viewers)
                            .hostedChannel(hostedChannel)
                            .build()
            );
        } else {
            event.getClient().getDispatcher().onNext(
                    UnhostEventImpl.builder()
                            .from(channelEvent)
                            .build()
            );
        }
    }

    private static void handleUserNotice(RawIRCEvent event) {
        ImmutableSet<Badge> badges = event.getTags().getBadges();
        int bits = Integer.parseInt(event.getTags().getOrDefault("bits", "0"));
        Color color = event.getTags().getColor();
        String displayName = event.getTags().get("display-name");
        ImmutableList<EmoteIndex> emotes = event.getTags().getEmotes();
        String message = parseMessage(event);
        boolean mod = event.getTags().getBoolean("mod");
        long channelId = event.getTags().getLong("room-id");
        Instant timestamp = event.getTags().getSentTimestamp();
        long userId = event.getTags().getLong("user-id");
        String userName = getUser(event);
        String channelName = getChannel(event);
        boolean actionMessage = hasAction(event);
        String messageId = event.getTags().get("msg-id");
        UserEvent userEvent = userEvent(event);
        ChannelEvent channelEvent = channelEvent(event);

        switch (messageId) {
            case "raid":
                long viewcount = event.getTags().getLong("msg-param-viewerCount");
                event.getClient().getDispatcher().onNext(
                        RaidEventImpl.of(viewcount, channelId, message, actionMessage, emotes, mod, badges, color, displayName, userId, timestamp, event.getClient(), channelName, userName)
                );
                break;
            case "ritual":
                String ritual = event.getTags().get("msg-param-ritual-name");
                if (ritual.equals("new_chatter")) {
                    event.getClient().getDispatcher().onNext(
                            NewChatterEventImpl.of(channelId, message, actionMessage, emotes, mod, badges, color, displayName, userId, timestamp, event.getClient(), channelName, userName)
                    );
                } else {
                    event.getClient().getDispatcher().onNext(
                            RitualNoticeEventImpl.of(channelId, message, actionMessage, emotes, mod, badges, color, displayName, userId, timestamp, event.getClient(), channelName, userName)
                    );
                }
                break;
            case "sub":
            case "resub":
            case "subgift":
                SubscriptionType subType = SubscriptionType.from(event.getTags().get("msg-param-sub-plan"));
                int months = (event.getTags().containsKey("msg-param-months") && !event.getTags().get("msg-param-months").equals("0")) ? Integer.parseInt(event.getTags().get("msg-param-months")) : 1;
                String gifter = (event.getTags().containsKey("msg-param-recipient-user-name") && !event.getTags().get("msg-param-recipient-user-name").equals("")) ? event.getTags().get("msg-param-recipient-user-name") : null;

                event.getClient().getDispatcher().onNext(
                        SubscribeEventImpl.of(months, gifter, subType, channelId, message, actionMessage, emotes, mod, badges, color, displayName, userId, timestamp, event.getClient(), channelName, userName)
                );
                break;
            case "submysterygift":
                SubscriptionType massSubType = SubscriptionType.from(event.getTags().get("msg-param-sub-plan"));
                int giftCount = (event.getTags().containsKey("msg-param-mass-gift-count")) ? Integer.parseInt(event.getTags().get("msg-param-mass-gift-count")) : 0;
                int totalGiftCount = (event.getTags().containsKey("msg-param-sender-count")) ? Integer.parseInt(event.getTags().get("msg-param-sender-count")) : 0;
                event.getClient().getDispatcher().onNext(
                        SubscribeGiftEventImpl.of(massSubType, giftCount, totalGiftCount, channelName, timestamp, event.getClient(), userName)
                );
                break;
        }
    }

    private static JoinChannelEvent joinEvent(RawIRCEvent event) {
        return JoinChannelEventImpl.builder()
                .from(channelEvent(event))
                .from(userEvent(event))
                .build();
    }

    private static PartChannelEvent partEvent(RawIRCEvent event) {
        return PartChannelEventImpl.builder()
                .from(channelEvent(event))
                .from(userEvent(event))
                .build();
    }

    private static PrivateMessageEventImpl privateMessage(RawIRCEvent event) {
        return PrivateMessageEventImpl
                .builder()
                .from(userEvent(event))
                .message(parseMessage(event))
                .build();
    }

    private static PingEvent<GlitchChat> pingEvent(RawIRCEvent event) {
        return PingEventImpl.<GlitchChat>builder()
                .client(event.getClient())
                .build();
    }

    private static PongEvent<GlitchChat> pongEvent(RawIRCEvent event) {
        return PongEventImpl.<GlitchChat>builder()
                .client(event.getClient())
                .build();
    }

    private static UserNoticeEvent subRaidRitualEvent(RawIRCEvent event) {
        return UserNoticeEventImpl.builder()
                .from(channelEvent(event))
                .tags(event.getTags())
                .message(parseMessage(event))
                .build();
    }

    private static NoticeEvent noticeEvent(RawIRCEvent event) {
        return NoticeEventImpl.builder()
                .from(channelEvent(event))
                .messageId(event.getTags().getOrDefault("msg-id", "unknown-id"))
                .message(parseMessage(event))
                .build();
    }

    @SuppressWarnings("unchecked")
    private static <E extends ChannelEvent> E clearChatEvent(RawIRCEvent event) {
        String userDirection = parseMessage(event);
        ChannelEvent channelEvent = channelEvent(event);
        if (!userDirection.equals("")) {
            String reason = event.getTags().getOrDefault("ban-reason", null);

            if (event.getTags().containsKey("ban-duration")) {
                return (E) TimeoutEventImpl.builder()
                        .from(channelEvent)
                        .username(userDirection)
                        .reason(reason)
                        .seconds(Long.parseLong(event.getTags().get("ban-duration")))
                        .build();
            } else {
                return (E) BanEventImpl.builder()
                        .from(channelEvent)
                        .username(userDirection)
                        .reason(reason)
                        .build();
            }
        } else {
            return (E) ClearChatEventImpl.builder()
                    .from(channelEvent)
                    .build();
        }
    }

    @SuppressWarnings("unchecked")
    private static <E extends ChannelEvent> E roomState(RawIRCEvent event) {
        ChannelEvent channelEvent = channelEvent(event);

        if (event.getTags().size() == 1) {
            String key = (String) event.getTags().keySet().toArray()[0];

            return (E) RoomStateChangedEventImpl.builder()
                    .from(channelEvent)
                    .key(key)
                    .value(event.getTags().get(key))
                    .build();
        } else {
            Locale broadcastLang = (event.getTags().get("broadcast-lang") != null && !event.getTags().get("broadcast-lang").equals("")) ? Locale.forLanguageTag(event.getTags().get("broadcast-lang")) : null;
            boolean r9k = event.getTags().get("r9k").equals("1");
            long slow = Long.parseLong(event.getTags().get("slow"));
            boolean subsOnly = event.getTags().get("subs-only").equals("1");
            return (E) RoomStateEventImpl.builder()
                    .from(channelEvent)
                    .broadcasterLanguage(broadcastLang)
                    .robot9000(r9k)
                    .slow(slow)
                    .subscribersOnly(subsOnly)
                    .build();
        }
    }

    private static GlobalUserStateEvent globalUserState(RawIRCEvent event) {
        return GlobalUserStateEventImpl.builder()
                .badges(event.getTags().getBadges())
                .userId(Long.parseLong(event.getTags().getOrDefault("user_id", "0")))
                .color(event.getTags().getColor())
                .displayName(event.getTags().get("display-name"))
                .build();
    }

    private static UserStateEvent userState(RawIRCEvent event) {
        return UserStateEventImpl.builder()
                .from(globalUserState(event))
                .from(channelEvent(event))
                .build();
    }

    private static UserEvent userEvent(RawIRCEvent event) {
        return UserEventImpl.builder()
                .createdAt(event.getCreatedAt())
                .client(event.getClient())
                .username(event.getPrefix().getUser())
                .build();

    }

    private static ChannelEvent channelEvent(RawIRCEvent event) {
        String user = event.getPrefix().getUser();
        return ChannelEventImpl.builder()
                .createdAt(event.getCreatedAt())
                .client(event.getClient())
                .channel(getChannel(event))
                .build();

    }

    @Nullable
    private static String getChannel(RawIRCEvent event) {
        return event.getMiddle()
                .stream()
                .filter(s -> s.startsWith("#"))
                .map(s -> s.substring(1))
                .findFirst().orElse(null);
    }

    private static String getUser(RawIRCEvent event) {
        if (event.getCommand().equals(IRCCommand.USER_NOTICE)) {
            return event.getTags().get("login");
        } else {
            return event.getPrefix().getUser();
        }
    }

    private static String parseMessage(RawIRCEvent event) {
        return event.getTrailing().replaceAll("(\\u0001|ACTION\\s)", "");
    }

    private static boolean hasAction(RawIRCEvent event) {
        return event.getTrailing().matches("\\u0001ACTION\\s(.*)\\u0001");
    }
}
