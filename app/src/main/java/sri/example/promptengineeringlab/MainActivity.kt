package sri.example.promptengineeringlab

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var taskInput: EditText

    private lateinit var zeroShotText: TextView
    private lateinit var fewShotText: TextView
    private lateinit var reasoningText: TextView

    private lateinit var zeroShotResponse: TextView
    private lateinit var fewShotResponse: TextView
    private lateinit var reasoningResponse: TextView

    private lateinit var comparisonText: TextView

    private val aiService: AIService = MockAIService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        taskInput = findViewById(R.id.taskInput)

        zeroShotText = findViewById(R.id.zeroShotText)
        fewShotText = findViewById(R.id.fewShotText)
        reasoningText = findViewById(R.id.reasoningText)

        zeroShotResponse = findViewById(R.id.zeroShotResponse)
        fewShotResponse = findViewById(R.id.fewShotResponse)
        reasoningResponse = findViewById(R.id.reasoningResponse)

        comparisonText = findViewById(R.id.comparisonText)

        val generateButton =
            findViewById<Button>(R.id.generateButton)

        val clearButton =
            findViewById<Button>(R.id.clearButton)

        generateButton.setOnClickListener {
            generatePrompts()
        }

        clearButton.setOnClickListener {
            clearScreen()
        }
    }

    private fun generatePrompts() {

        val task = taskInput.text.toString().trim()

        if (task.isEmpty()) {
            taskInput.error = "Please enter a task"
            return
        }

        val zeroShot = createZeroShotPrompt(task)

        val fewShot = createFewShotPrompt(task)

        val stepByStep = createStepByStepPrompt(task)

        zeroShotText.text = zeroShot
        fewShotText.text = fewShot
        reasoningText.text = stepByStep

        generateResponses(
            zeroShot,
            fewShot,
            stepByStep
        )
    }

    private fun createZeroShotPrompt(
        task: String
    ): String {

        return """
            Task:
            $task

            Answer the task clearly and accurately.
            Use simple language and organize the answer logically.
        """.trimIndent()
    }

    private fun createFewShotPrompt(
        task: String
    ): String {

        return """
            Task:
            $task

            Examples:

            Example 1:
            Question: What is AI?
            Answer: AI enables machines to perform tasks
            that normally require human intelligence.

            Example 2:
            Question: What is Machine Learning?
            Answer: Machine Learning allows computers
            to learn patterns from data.

            Now answer:

            $task

            Follow the style and structure of the examples.
        """.trimIndent()
    }

    private fun createStepByStepPrompt(
        task: String
    ): String {

        return """
            Task:
            $task

            Instructions:

            1. Identify the main requirement.
            2. Break the task into important parts.
            3. Consider the relevant concepts.
            4. Construct the answer logically.
            5. Provide a concise final response.

            Do not reveal private chain-of-thought.
            Give only the useful explanation and final answer.
        """.trimIndent()
    }

    private fun generateResponses(
        zeroShot: String,
        fewShot: String,
        stepByStep: String
    ) {

        zeroShotResponse.text = "Generating Zero-Shot response..."

        fewShotResponse.text = "Generating Few-Shot response..."

        reasoningResponse.text =
            "Generating Step-by-Step response..."

        lifecycleScope.launch {

            val startZeroShot = System.currentTimeMillis()

            val zeroResponse =
                aiService.generateResponse(zeroShot)

            val zeroTime =
                System.currentTimeMillis() - startZeroShot

            val startFewShot = System.currentTimeMillis()

            val fewResponse =
                aiService.generateResponse(fewShot)

            val fewTime =
                System.currentTimeMillis() - startFewShot

            val startStep = System.currentTimeMillis()

            val stepResponse =
                aiService.generateResponse(stepByStep)

            val stepTime =
                System.currentTimeMillis() - startStep

            zeroShotResponse.text =
                """
                ZERO-SHOT RESPONSE

                $zeroResponse

                Response Time: ${zeroTime} ms
                """.trimIndent()

            fewShotResponse.text =
                """
                FEW-SHOT RESPONSE

                $fewResponse

                Response Time: ${fewTime} ms
                """.trimIndent()

            reasoningResponse.text =
                """
                STEP-BY-STEP RESPONSE

                $stepResponse

                Response Time: ${stepTime} ms
                """.trimIndent()

            updateComparison(
                zeroTime,
                fewTime,
                stepTime
            )
        }
    }

    private fun updateComparison(
        zeroTime: Long,
        fewTime: Long,
        stepTime: Long
    ) {

        comparisonText.text =
            """
            RESPONSE COMPARISON

            ZERO-SHOT
            Response Time: ${zeroTime} ms

            FEW-SHOT
            Response Time: ${fewTime} ms

            STEP-BY-STEP
            Response Time: ${stepTime} ms


            EVALUATION CRITERIA

            • Accuracy
            • Relevance
            • Clarity
            • Completeness
            • Consistency
            • Response Time


            STATUS

            ✓ Prompt generation: Working
            ✓ AI service layer: Working
            ✓ Response cards: Working
            ✓ Response timing: Working

            AI MODEL:
            Not connected yet
            """.trimIndent()
    }

    private fun clearScreen() {

        taskInput.text.clear()

        zeroShotText.text =
            "No examples are provided. The model receives only the task."

        fewShotText.text =
            "Examples are provided to guide the expected response."

        reasoningText.text =
            "The task is divided into logical steps."

        zeroShotResponse.text =
            "Zero-Shot Response Preview"

        fewShotResponse.text =
            "Few-Shot Response Preview"

        reasoningResponse.text =
            "Step-by-Step Response Preview"

        comparisonText.text =
            "Generate prompts to see the comparison."
    }
}