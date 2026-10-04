package sri.example.promptengineeringlab

interface AIService {

    suspend fun generateResponse(
        prompt: String
    ): String
}