package glitch.kraken.object.json;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CommercialData {
    @SerializedName("Length")
    private final int length;
    @SerializedName("Message")
    private final String message;
    @SerializedName("RetryAfter")
    private final int retryAfter;
}
