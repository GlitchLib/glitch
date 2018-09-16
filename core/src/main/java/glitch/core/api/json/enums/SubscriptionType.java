package glitch.core.api.json.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SubscriptionType {
    TIER1("1000"),
    TIER2("2000"),
    TIER3("3000"),
    PRIME("Prime"),
    UNKNOWN("");

    @Getter
    private final String value;

    public static SubscriptionType from(String type) {
        for (SubscriptionType subscriptionType : values()) {
            if (subscriptionType.value.equalsIgnoreCase(type)) {
                return subscriptionType;
            }
        }

        return UNKNOWN;
    }
}
