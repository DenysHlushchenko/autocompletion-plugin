import io.github.ollama4j.OllamaAPI
import io.github.ollama4j.types.OllamaModelType
import io.github.ollama4j.utils.OptionsBuilder

class OllamaClient {
    private val ollamaAPI = OllamaAPI("http://localhost:11434").apply {
        setRequestTimeoutSeconds(60);
    }

    /**
     * Get suggestions for the next word in Java code.
     * @param word The word to get suggestions for.
     * @return A list of suggestions.
     */
    fun getSuggestions(word: String): List<String> {
        return try {
            val prompt = "Provide a single Java keyword or method name that starts with \"$word\" and is used in a method context. " +
                    "Do not include any explanations, newlines, text or extra characters. Output only the single word."
            val options = OptionsBuilder().setNumPredict(20).build()
            val response = ollamaAPI.generate(OllamaModelType.CODELLAMA, prompt, false, options)
            val suggestion = response.response.trim()
            listOf(suggestion)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

//fun main() {
//    val client = OllamaClient();
//    val suggestions = client.getSuggestions("me");
//    println("RESULT: $suggestions");
//}

// type in terminal 'ollama serve' must be running on http://localhost:11434
// test in terminal with './gradlew runMain'