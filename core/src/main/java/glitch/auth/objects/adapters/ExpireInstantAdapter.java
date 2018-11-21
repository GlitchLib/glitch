package glitch.auth.objects.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class ExpireInstantAdapter extends TypeAdapter<Instant> {
    @Override
    public void write(JsonWriter out, Instant value) throws IOException {
        long time = value.getEpochSecond() - System.currentTimeMillis();
        out.value((time > 0L) ? time : 0);
    }

    @Override
    public Instant read(JsonReader in) throws IOException {
        return Instant.now().plus(in.nextLong(), ChronoUnit.SECONDS);
    }
}
