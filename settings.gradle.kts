rootProject.name = "glitch"

// TODO Inject those libraries
//include "auth:ktor", "auth:spring", "auth:play", "auth:vertx", "auth:akka", "auth:http4k"

include(*moduleList)

include("all", "bom")