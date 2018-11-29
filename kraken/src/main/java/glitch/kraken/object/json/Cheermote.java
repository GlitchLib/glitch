package glitch.kraken.object.json;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import glitch.api.objects.json.interfaces.IDObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.util.Arrays;

@Data
public class Cheermote {
    private final String prefix;
    private final ImmutableList<Tier> tiers;

    @Data
    public static class Tier implements IDObject<Long> {
        private final Long id;
        private final Color color;
        private final ImmutableMap<Background, ImmutableMap<Type, ImmutableMap<Size, String>>> images;

        public String getImage(Background background, Type type, Size size) {
            return images.get(background).get(type).get(size);
        }
    }

    public enum Background {
        LIGHT,
        DARK
    }

    public enum Type {
        STATIC,
        ANIMATED
    }

    @RequiredArgsConstructor
    public enum Size {
        X1(1),
        X15(1.5),
        X2(2),
        X3(3),
        X4(4);

        private final double value;

        public static Size of(double size) {
            return Arrays.stream(values()).filter(s -> s.value == size)
                    .findFirst().orElseThrow(() -> new NullPointerException("Cannot obtain requested size: " + size));
        }
    }
}
