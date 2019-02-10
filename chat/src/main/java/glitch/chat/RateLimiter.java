package glitch.chat;

import java.io.Closeable;

/**
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
public class RateLimiter implements Runnable, Closeable {

    //    private final Queue<Whisper> whispers = new SynchronousQueue<>();
//    private final Queue<String> chat = new SynchronousQueue<>();
//
//    private final int whisperDailyCount;
//
//    private final Duration chatDuration, whisperDuration;
//
//    private final Thread whisper;
//
//    public RateLimiter(Duration chatDuration, Duration whisperDuration, int whisperDailyCount, GlitchChat client) {
//        this.whisperDailyCount = whisperDailyCount;
//        this.chatDuration = chatDuration;
//        this.whisperDuration = whisperDuration;
//
//        this.channel = new Thread(() -> doChat(client), "GlitchChannelLimiter");
//        this.whisper = new Thread(() -> doWhisper(client), "GlitchWhisperLimiter");
//    }
//
//    void doChat(GlitchChat client) {
//        while (true) {
//            try {
//                if (!chat.isEmpty()) {
//                    client.send(chat.poll());
//                    Thread.sleep(chatDuration.toMillis());
//                }
//            } catch (InterruptedException e) {
//                chat.clear();
//                break;
//            }
//        }
//    }
//
//    void doWhisper(GlitchChat client) {
//        while (true) {
//            try {
//                if (!whispers.isEmpty()) {
//                    client.ws.send(Mono.just(whispers.poll().toString()));
//                    Thread.sleep(whisperDuration.toMillis());
//                }
//            } catch (InterruptedException e) {
//                chat.clear();
//                break;
//            }
//        }
//    }
//
//    static RateLimiter ofUserState(GlobalUserState gus, GlitchChat chat) {
//        if (gus != null) {
//            if (gus.isVerifiedBot()) {
//                return verifiedBot(chat);
//            } else if (gus.isKnownBot()) {
//                return knownBot(chat);
//            }
//        }
//
//        return global(chat);
//    }
//
//    /**
//     * Brought you by Global Rate limits
//     */
//    static RateLimiter global(GlitchChat chat) {
//        return new RateLimiter(Duration.ofMillis(30000 / 20), Duration.ofMillis(60000 / 100), 40, chat);
//    }
//
//    /**
//     * Brought you by Know Bot Rate limits
//     */
//    static RateLimiter knownBot(GlitchChat chat) {
//        return new RateLimiter(Duration.ofMillis(30000 / 50), Duration.ofMillis(60000 / 200), 500, chat);
//    }
//
//    /**
//     * Brought you by Verified Bot Rate limits
//     */
//    static RateLimiter verifiedBot(GlitchChat chat) {
//        return new RateLimiter(Duration.ofMillis(30000 / 7500), Duration.ofMillis(60000 / 1200), 100000, chat);
//    }
//
//    void directMessage(String username, String message) {
//        if (whispersCache.size() >= whisperDailyCount) {
//            throw new WhispersExceededException(whisperDailyCount);
//        }
//
//        if (!whispersCache.containsKey(username)) {
//            whispersCache.put(username, message);
//        }
//
//        whispers.add(new Whisper(username, message));
//    }
//
//    public void message(String channel, String message, boolean botMod) {
//        chat.add(String.format("PRIVMSG #%s %s", channel, message.startsWith("/") ? message : ":" + message));
//    }
//
    @Override
    public void run() {
//        this.whisper.run();
    }

    @Override
    public void close() {
//        this.whisper.interrupt();
    }
//
//    private class Whisper {
//        private final String username;
//        private final String message;
//
//        public Whisper(String username, String message) {
//            this.username = username;
//            this.message = message;
//        }
//
//        @Override
//        public String toString() {
//            return String.format("PRIVMSG #jtv /w %s %s", username, message);
//        }
//    }
}
