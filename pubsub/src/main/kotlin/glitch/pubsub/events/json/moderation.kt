package glitch.pubsub.events.json

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import glitch.api.objects.json.interfaces.IDObject
import glitch.pubsub.`object`.adapters.ModerationActionAdapter
import java.util.*

/**
 *
 * @author Damian Staszewski [damian@stachuofficial.tv]
 * @version %I%, %G%
 * @since 1.0
 */
data class Timeout(
        override val moderatorName: String,
        override val moderatorId: Long,
        override val targetName: String,
        override val targetId: Long,
        val duration: Int,
        val reason: String?
) : IModerator, ITarget {
    constructor(data: ModerationData) : this(
            data.createdBy,
            data.createdById,
            data.args[0],
            data.targetId,
            data.args[1].toInt(),
            if (data.args.size > 2) data.args[2] else null
    )
}

data class Ban(
        override val moderatorName: String,
        override val moderatorId: Long,
        override val targetName: String,
        override val targetId: Long,
        val reason: String?
) : IModerator, ITarget {
    constructor(data: ModerationData) : this(
            data.createdBy,
            data.createdById,
            data.args[0],
            data.targetId,
            if (data.args.size > 1) data.args[1] else null
    )
}

data class Host(
        override val moderatorName: String,
        override val moderatorId: Long,
        override val targetName: String,
        override val targetId: Long
) : IModerator, ITarget {
    constructor(data: ModerationData) : this(
            data.createdBy,
            data.createdById,
            data.args[0],
            data.targetId
    )
}

data class MessageDelete(
        override val id: UUID,
        override val moderatorName: String,
        override val moderatorId: Long,
        override val targetName: String,
        override val targetId: Long,
        val message: String
) : IDObject<UUID>, IModerator, ITarget {
    constructor(data: ModerationData) : this(
            UUID.fromString(data.args[2]),
            data.createdBy,
            data.createdById,
            data.args[0],
            data.targetId,
            data.args[1]
    )
}

data class ModerationData(
        val type: String,
        @JsonAdapter(ModerationActionAdapter::class)
        val moderationAction: Action,
        val args: List<String>,
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

data class Moderator(
        override val moderatorName: String,
        override val moderatorId: Long
) : IModerator {
    constructor(data: ModerationData) : this(
            data.createdBy,
            data.createdById
    )
}

data class ActivationByMod(
        override val moderatorName: String,
        override val moderatorId: Long,
        @get:SerializedName("active")
        val isActive: Boolean
) : IModerator {
    constructor(data: ModerationData, isActive: Boolean) : this(
            data.createdBy,
            data.createdById,
            isActive
    )
}

data class Unban(
        override val moderatorName: String,
        override val moderatorId: Long,
        override val targetName: String,
        override val targetId: Long
) : IModerator, ITarget {
    constructor(data: ModerationData) : this(
            data.createdBy,
            data.createdById,
            data.args[0],
            data.targetId
    )
}