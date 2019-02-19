package glitch.api.http

data class TestingResponse(
        val primary: String,
        val isSecondary: Boolean,
        val tertiary: Long,
        val quaternary: Any?
)
