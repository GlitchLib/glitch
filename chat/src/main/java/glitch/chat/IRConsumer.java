package glitch.chat;

import glitch.chat.cache.Channel;
import glitch.chat.events.*;
import glitch.core.api.json.Badge;
import glitch.core.api.json.BadgeImpl;
import glitch.core.api.json.enums.SubscriptionType;
import glitch.socket.events.actions.PingEvent;
import glitch.socket.events.actions.PingEventImpl;
import glitch.socket.events.actions.PongEvent;
import glitch.socket.events.actions.PongEventImpl;
import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;
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
                event.getClient().getDispatcher().onNext(messageEvent(event));
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
                event.getClient().getDispatcher().onNext(subRaidRitualEvent(event));
                break;
            case NOTICE:
                event.getClient().getDispatcher().onNext(noticeEvent(event));
                break;
            case HOST_TARGET:
                event.getClient().getDispatcher().onNext(host(event));
                break;
            case CLEAR_CHAT:
                event.getClient().getDispatcher().onNext(clearChatEvent(event));
                break;
            case ROOM_STATE:
                event.getClient().getDispatcher().onNext(roomState(event));
                break;
            case GLOBAL_USER_STATE:
                event.getClient().getDispatcher().onNext(globalUserState(event));
                break;
            case USER_STATE:
                event.getClient().getDispatcher().onNext(userState(event));
                break;
        }
    }

    public static void composeFrom(MessageEvent event) {
        ChannelMessageEvent channelMessageEvent = channelMessageEvent(event);

        int bits = event.getTags().containsKey("bits") ? Integer.parseInt(event.getTags().get("bits")) : 0;

        if (bits > 0) {
            event.getClient().getDispatcher().onNext(BitsMessageEventImpl.builder().from(channelMessageEvent).bits(bits).build());
        } else if (event.isActionMessage()) {
            event.getClient().getDispatcher().onNext(ActionMessageEventImpl.builder().from(channelMessageEvent).build());
        } else {
            event.getClient().getDispatcher().onNext(channelMessageEvent);
        }
    }

    public static void composeFrom(UserNoticeEvent event) {
        Set<Badge> badges = badges(event.getTags().get("badges"));
        Color color = getColor(event.getTags().get("color"));
        String user = event.getTags().get("login");
        String displayName = event.getTags().get("display-name");
        Long userId = Long.parseLong(event.getTags().get("user-id"));
        String messageId = event.getTags().get("msg-id");
        Long channelId = Long.parseLong(event.getTags().get("room-id"));

        switch (messageId) {
            case "raid":
                long viewcount = Long.parseLong(event.getTags().get("msg-param-viewerCount"));
                event.getClient().getDispatcher().onNext(
                        RaidEventImpl.builder()
                                .from(event)
                                .badges(badges)
                                .color(color)
                                .username(user)
                                .displayName(displayName)
                                .userId(userId)
                                .channelId(channelId)
                                .viewcount(viewcount)
                                .message(event.getMessage())
                                .build()
                );
                break;
            case "ritual":
                String ritual = event.getTags().get("msg-param-ritual-name");
                if (ritual.equals("new_chatter")) {
                    event.getClient().getDispatcher().onNext(
                            NewChatterEventImpl.builder()
                                    .from(event)
                                    .badges(badges)
                                    .color(color)
                                    .username(user)
                                    .displayName(displayName)
                                    .userId(userId)
                                    .channelId(channelId)
                                    .message(event.getMessage())
                                    .build()
                    );
                } else {
                    event.getClient().getDispatcher().onNext(
                            RitualNoticeEventImpl.builder()
                                    .from(event)
                                    .badges(badges)
                                    .color(color)
                                    .username(user)
                                    .displayName(displayName)
                                    .userId(userId)
                                    .channelId(channelId)
                                    .message(event.getMessage())
                                    .build()
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
                        SubscribeEventImpl.builder()
                                .from(event)
                                .badges(badges)
                                .color(color)
                                .username(user)
                                .displayName(displayName)
                                .userId(userId)
                                .channelId(channelId)
                                .message(event.getMessage())
                                .gifterUsername(gifter)
                                .subscriptionType(subType)
                                .months(months)
                                .build()
                );
                break;
            case "submysterygift":
                SubscriptionType massSubType = SubscriptionType.from(event.getTags().get("msg-param-sub-plan"));
                int giftCount = (event.getTags().containsKey("msg-param-mass-gift-count")) ? Integer.parseInt(event.getTags().get("msg-param-mass-gift-count")) : 0;
                int totalGiftCount = (event.getTags().containsKey("msg-param-sender-count")) ? Integer.parseInt(event.getTags().get("msg-param-sender-count")) : 0;
                event.getClient().getDispatcher().onNext(
                        SubscribeGiftEventImpl.builder()
                                .from(event)
                                .username(user)
                                .subscriptionType(massSubType)
                                .giftedCount(giftCount)
                                .totalGiftedCount(totalGiftCount)
                                .build()
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

    private static MessageEvent messageEvent(RawIRCEvent event) {
        return MessageEventImpl.builder()
                .from(userEvent(event))
                .from(channelEvent(event))
                .tags(event.getTags())
                .message(parseMessage(event))
                .isActionMessage(hasAction(event))
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
    private static <E extends ChannelEvent> E host(RawIRCEvent event) {
        String message = parseMessage(event);
        boolean host = message.equals("");
        String hostedChannel = event.getTrailing().get(1);
        ChannelEvent channelEvent = channelEvent(event);
        long viewers = Long.parseLong(event.getTrailing().get(2).substring(1).replace("]", ""));
        if (host) {
            return (E) HostEventImpl.builder()
                    .from(channelEvent)
                    .viewers(viewers)
                    .hostedChannel(hostedChannel)
                    .build();
        } else {
           return (E) UnhostEventImpl.builder()
                   .from(channelEvent)
                   .build();
        }
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
                .badges(badges(event.getTags().get("badges")))
                .userId(Long.parseLong(event.getTags().getOrDefault("user_id", "0")) )
                .color(getColor(event.getTags().get("color")))
                .displayName(event.getTags().get("display-name"))
                .build();
    }

    private static UserStateEvent userState(RawIRCEvent event) {
        return UserStateEventImpl.builder()
                .from(globalUserState(event))
                .from(channelEvent(event))
                .build();
    }

    private static ChannelMessageEvent channelMessageEvent(MessageEvent event) {
        return ChannelMessageEventImpl.builder()
                .channelId(Long.parseLong(event.getTags().get("room-id")))
                .userId(Long.parseLong(event.getTags().get("user-id")))
                .displayName(event.getTags().get("display-name"))
                .badges(badges(event.getTags().get("badges")))
                .color(getColor(event.getTags().get("color")))
                .message(event.getMessage())
                .from((ChannelEvent) event)
                .from((UserEvent) event)
                .build();
    }

    private static Set<Badge> badges(String badges) {
        return Arrays.stream(badges.split(","))
                .map(badge -> BadgeImpl.of(Integer.parseInt(badge.split("/")[1]), badge.split("/")[0]))
                .collect(Collectors.toSet());
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
        return event.getTrailing()
                .stream()
                .filter(s -> s.startsWith("#"))
                .map(s -> s.substring(1))
                .findFirst().orElse(null);
    }

    private static String parseMessage(RawIRCEvent event) {
        return String.join(" ", event.getMiddle()).replaceAll("\\u0001", "").replaceAll("ACTION ", "").trim();
    }

    private static boolean hasAction(RawIRCEvent event) {
        return event.getMiddle().contains("ACTION");
    }

    private static Color getColor(String color) {
        return Color.decode(color);
    }
}
