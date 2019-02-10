package glitch.pubsub.`object`.json

data class Ban(
        val moderatorName: String, val moderatorId: Long,
        val targetName: String, val targetId: Long, val reason: String?
)
