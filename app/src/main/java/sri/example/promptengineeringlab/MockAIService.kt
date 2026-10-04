package sri.example.promptengineeringlab

class MockAIService : AIService {

    override suspend fun generateResponse(
        prompt: String
    ): String {

        return """
            This is a sample AI response.

            The actual AI model will answer this prompt
            after API integration is enabled.

            Current prompt length:
            ${prompt.length} characters.
        """.trimIndent()
    }
}