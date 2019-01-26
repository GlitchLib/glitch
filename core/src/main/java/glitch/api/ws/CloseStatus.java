package glitch.api.ws;

import java.util.Objects;

/**
 * Close Status POJO
 */
public class CloseStatus {
    private final int code;
    private final String reason;

    public CloseStatus(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CloseStatus)) return false;
        CloseStatus that = (CloseStatus) o;
        return getCode() == that.getCode() &&
                Objects.equals(getReason(), that.getReason());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getReason());
    }

    @Override
    public String toString() {
        return "CloseStatus{" +
                "code=" + code +
                ", reason='" + reason + '\'' +
                '}';
    }
}
