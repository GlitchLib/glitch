package glitch.helix.object.json;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Bits {
    private final Long userId;
    @SerializedName("user_name")
    private final String username;
    private final int rank;
    private final int score;
}
