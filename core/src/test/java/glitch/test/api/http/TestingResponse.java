package glitch.test.api.http;

import java.util.Objects;

public class TestingResponse {
    private final String primary;
    private final boolean secondary;
    private final long tertiary;
    private final Object quaternary;

    public TestingResponse(String primary, boolean secondary, long tertiary, Object quaternary) {
        this.primary = primary;
        this.secondary = secondary;
        this.tertiary = tertiary;
        this.quaternary = quaternary;
    }

    public String getPrimary() {
        return primary;
    }

    public boolean isSecondary() {
        return secondary;
    }

    public long getTertiary() {
        return tertiary;
    }

    public Object getQuaternary() {
        return quaternary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestingResponse)) return false;
        TestingResponse that = (TestingResponse) o;
        return isSecondary() == that.isSecondary() &&
                getTertiary() == that.getTertiary() &&
                Objects.equals(getPrimary(), that.getPrimary()) &&
                Objects.equals(getQuaternary(), that.getQuaternary());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrimary(), isSecondary(), getTertiary(), getQuaternary());
    }

    @Override
    public String toString() {
        return "TestingResponse{" +
                "primary='" + primary + '\'' +
                ", secondary=" + secondary +
                ", tertiary=" + tertiary +
                ", quaternary=" + quaternary +
                '}';
    }
}
