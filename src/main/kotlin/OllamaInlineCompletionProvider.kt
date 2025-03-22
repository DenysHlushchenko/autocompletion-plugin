import com.intellij.codeInsight.inline.completion.InlineCompletionEvent
import com.intellij.codeInsight.inline.completion.InlineCompletionProvider
import com.intellij.codeInsight.inline.completion.InlineCompletionProviderID
import com.intellij.codeInsight.inline.completion.InlineCompletionRequest
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionGrayTextElement
import com.intellij.codeInsight.inline.completion.suggestion.InlineCompletionSingleSuggestion
import com.intellij.codeInsight.inline.completion.suggestion.InlineCompletionSuggestion

class OllamaInlineCompletionProvider: InlineCompletionProvider {
    override val id: InlineCompletionProviderID = InlineCompletionProviderID("OllamaInlineCompletionProvider")

    private val cache = Trie();
    private val ollamaServer = OllamaServer();

    /**
     * Gets a suggestion for the current word at the cursor.
     * @param request The InlineCompletionRequest.
     * @return An InlineCompletionSuggestion.
     */
    override suspend fun getSuggestion(request: InlineCompletionRequest): InlineCompletionSuggestion {
        val document = request.editor.document;
        val offset = request.endOffset;

        val word = getWordAtCursor(document.text, offset);
        if (word.isEmpty()) return InlineCompletionSuggestion.Empty;

        val cachedSuggestions = cache.getSuggestions(word);
        if (cachedSuggestions.isNotEmpty()) {
            return buildSuggestion(cachedSuggestions.first(), word);
        };

        val generatedSuggestion = ollamaServer.generateCompletion(word);
        if (generatedSuggestion.isNotEmpty()) {
            cache.insert(word, listOf(generatedSuggestion));
            return buildSuggestion(generatedSuggestion, word);
        };

        return InlineCompletionSuggestion.Empty;
    };

    override fun isEnabled(event: InlineCompletionEvent): Boolean {
        return true;
    };

    /**
     * Builds an InlineCompletionSuggestion from a suggestion and a word.
     * @param suggestion The suggestion from the Ollama model.
     * @param word Current word at cursor.
     * @return An InlineCompletionSuggestion.
     */
    private fun buildSuggestion(suggestion: String, word: String): InlineCompletionSuggestion {
        val completionText = suggestion.substring(word.length);
        if (completionText.isBlank()) return InlineCompletionSuggestion.Empty;
        return InlineCompletionSingleSuggestion.build {
            emit(InlineCompletionGrayTextElement(completionText));
        };
    };

    /**
     * Extracts the word being typed at the cursor's position.
     * @param text The text in the editor.
     * @param offset Position within the text.
     * @return The word at the cursor.
     */
    private fun getWordAtCursor(text: String, offset: Int): String {
        if (offset == 0) return "";
        val beforeCursor = text.substring(0, offset);
        val lastWord = beforeCursor.split(Regex("\\s+")).lastOrNull() ?: "";
        return lastWord.trim();
    };
}

class OllamaServer() {
    private val client = OllamaClient();

    suspend fun generateCompletion(word: String): String {
        val suggestions = client.getSuggestions(word);
        return suggestions.firstOrNull() ?: "";
    };
}