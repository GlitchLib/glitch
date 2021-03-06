rootProject.name = "glitch"

include(
        "auth:ktor", "auth:spring",
        "auth:play", "auth:vertx",
        "auth:akka", "auth:http4k"
)

include("core", "chat", "kraken", "helix", "pubsub", "webhook")

include("all", "bom")