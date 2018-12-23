package glitch.api.objects.enums;

public enum UserType {
    USER,
    MOD,
    @Deprecated
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
