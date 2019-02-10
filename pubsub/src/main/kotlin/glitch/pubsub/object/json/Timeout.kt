package glitch.pubsub.`object`.json

class Timeout(
        val moderatorName: String, val moderatorId: Long,
        val targetName: String, val targetId: Long,
        val duration: Int, val reason: String?
) {

}
