package io.glitchlib.model

enum class BroadcasterType {
    NONE,
    AFFILIATE,
    PARTNER
}

enum class SubscriptionType(val value: String) {
    UNKNOWN(""),
    PRIME("Prime"),
    TIER1("1000"),
    TIER2("2000"),
    TIER3("3000");


    companion object {

        fun from(type: String): SubscriptionType {
            for (subscriptionType in values()) {
                if (subscriptionType.value.equals(type, ignoreCase = true)) {
                    return subscriptionType
                }
            }

            return UNKNOWN
        }
    }
}

enum class UserType {
    USER,
    MOD,
    @Deprecated("")
    GLOBAL_MOD,
    ADMIN,
    STAFF;


    companion object {
        fun from(userType: String?): UserType {
            if (userType == null) {
                return USER
            }

            for (type in values()) {
                if (type.name.equals(userType, ignoreCase = true)) {
                    return type
                }
            }
            return USER
        }
    }
}

enum class VideoType {
    ARCHIVE, HIGHLIGHT, UPLOAD, ALL
}

enum class ViewType {
    PUBLIC,
    PRIVATE
}

