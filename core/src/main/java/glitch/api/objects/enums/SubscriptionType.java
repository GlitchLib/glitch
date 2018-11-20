package glitch.api.objects.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SubscriptionType {
    UNKNOWN(""),
    PRIME("Prime"),
    TIER1("1000"),
    TIER2("2000"),
    TIER3("3000");

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