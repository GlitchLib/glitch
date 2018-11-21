package glitch.pubsub.object.json;

import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import glitch.pubsub.object.adapters.ModerationActionAdapter;
import lombok.Data;

@Data
public class ModerationData {
    private final String type;
    @JsonAdapter(ModerationActionAdapter.class)
    private final Action moderationAction;
    private final ImmutableList<String> args;
    private final String createdBy;
    @SerializedName("created_by_user_id")
    private final Long createdById;
    @SerializedName("target_user_id")
    private final Long targetId;

    public enum Action {
        TIMEOUT,
        BAN,
        UNBAN,
        UNTIMEOUT,
        HOST,
        SUBSCRIBERS,
        SUBSCRIBERSOFF,
        CLEAR,
        EMOTEONLY,
        EMOTEONLYOFF,
        R9KBETA,
        R9KBETAOFF
    }
}
