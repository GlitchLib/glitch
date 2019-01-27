package glitch.api.objects.enums;

public enum SubscriptionType {
    UNKNOWN(""),
    PRIME("Prime"),
    TIER1("1000"),
    TIER2("2000"),
    TIER3("3000");

    private final String value;

    SubscriptionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SubscriptionType from(String type) {
        for (SubscriptionType subscriptionType : values()) {
            if (subscriptionType.value.equalsIgnoreCase(type)) {
                return subscriptionType;
            }
        }

        return UNKNOWN;
    }
}