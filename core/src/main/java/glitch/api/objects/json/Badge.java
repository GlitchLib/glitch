package glitch.api.objects.json;

import java.util.Objects;

public class Badge {
    private final String name;
    private final int version;

    public Badge(String name, int version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Badge)) return false;
        Badge badge = (Badge) o;
        return getVersion() == badge.getVersion() &&
                getName().equals(badge.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getVersion());
    }

    @Override
    public String toString() {
        return "Badge{" +
                "name='" + name + '\'' +
                ", version=" + version +
                '}';
    }
}
