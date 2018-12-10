package glitch.helix.object.json.list;

import com.google.common.collect.ImmutableList;
import glitch.api.objects.json.interfaces.OrdinalList;
import glitch.helix.object.json.Bits;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;

@Data
public class BitsLeaderboard implements OrdinalList<Bits> {
    private final ImmutableList<Bits> data;
    private final DateRange dateRange;
    @Getter(AccessLevel.NONE)
    private final int total;

    @Override
    public int size() {
        return total;
    }

    @Data
    public class DateRange {
        private final Instant startedAt;
        private final Instant endedAt;
    }
}
