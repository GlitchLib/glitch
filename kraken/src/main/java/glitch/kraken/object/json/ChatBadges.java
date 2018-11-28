package glitch.kraken.object.json;

import lombok.Data;

@Data
public class ChatBadges {
    private final Images admin;
    private final Images broadcaster;
    private final Images globalMod;
    private final Images mod;
    private final Images staff;
    private final Images subscriber;
    private final Images turbo;

    @Data
    public static class Images {
        private final String alpha;
        private final String image;
        private final String svg;
    }
}
