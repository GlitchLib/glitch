package glitch.kraken.enums;

public enum UserType {
    NONE,
    MOD,
    GLOBAL_MOD,
    ADMIN,
    STAFF;

    public static UserType from(String userType) {
        for (UserType type: values()) {
            if (type.name().equalsIgnoreCase(userType)) {
                return type;
            }
        }
        return NONE;
    }
}
