package glitch.pubsub.`object`.json

import java.time.Instant

class WhisperThread(
        val isArchived: Boolean, val isMuted: Boolean,
        val whitelistedUntil: Instant
) {

}
