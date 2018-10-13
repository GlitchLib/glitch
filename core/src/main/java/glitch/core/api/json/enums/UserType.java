package glitch.core.api.json.enums;

public enum UserType {
    USER,
    MOD,
    GLOBAL_MOD,
    ADMIN,
    STAFF;

    public static UserType from(String userType) {
        for (UserType type : values()) {
            if (type.name().equalsIgnoreCase(userType)) {
                return type;
            }
        }
        return USER;
    }
}
