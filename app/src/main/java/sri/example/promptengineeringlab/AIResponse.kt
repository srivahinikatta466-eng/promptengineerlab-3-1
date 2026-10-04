package sri.example.promptengineeringlab

data class AIResponse(
    val technique: String,
    val response: String,
    val responseTime: Long = 0L
)