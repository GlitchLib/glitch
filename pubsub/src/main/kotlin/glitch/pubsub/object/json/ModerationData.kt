package glitch.pubsub.`object`.json

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import glitch.pubsub.`object`.adapters.ModerationActionAdapter

class ModerationData(
        val type: String?, @JsonAdapter(ModerationActionAdapter::class)
        val moderationAction: Action, val args: List<String>,
        val createdBy: String,
        @SerializedName("created_by_user_id")
        val createdById: Long,
        @SerializedName("target_user_id")
        val targetId: Long
) {
    enum class Action {
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
        R9KBETAOFF,
        DELETE
    }
}
