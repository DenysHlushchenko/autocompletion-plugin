import org.junit.Test
import kotlin.test.*;

class TestTrie {
    private lateinit var trie: Trie;

    @BeforeTest
    fun setUp() {
        trie = Trie();
    }

    @Test
    fun GivenEmptyTrie_WhenInsertingChars_ConfirmIsCorrect() {
        trie.insert("static", listOf("static"));
        trie.insert("public", listOf("public"));

        assertTrue(trie.getSize() == 13);

        println(trie.printTrie());
    }

    @Test
    fun GivenEmptyTrie_WhenInsertingWord_ConfirmSuggestionsAreStored() {
        trie.insert("Str", listOf("String"));

        val suggestions = trie.getSuggestions("Str");
        assertEquals(listOf("String"), suggestions);
    }

    @Test
    fun GivenEmptyTrie_WhenInsertingIncorrectWord_ConfirmSuggestionsAreEmpty() {
        trie.insert("Str", listOf("String"));

        val suggestions1 = trie.getSuggestions("Stat");
        assertEquals(emptyList(), suggestions1);

        val suggestions2 = trie.getSuggestions("");
        assertEquals(emptyList(), suggestions2);
    }

    @Test
    fun GivenEmptyTrie_WhenInsertingMultipleSuggestions_ConfirmSuggestionsAreCorrect() {
        trie.insert("p", listOf("public"));
        trie.insert("p", listOf("private"));

        val suggestions = trie.getSuggestions("p");
        assertEquals(listOf("private", "public"), suggestions);
    }

    @Test
    fun GivenEmptyTrie_WhenInsertingDuplicates_ConfirmSuggestionsAreCorrect() {
        trie.insert("pub", listOf("public"));
        trie.insert("pub", listOf("public"));

        val suggestions = trie.getSuggestions("pub");
        assertEquals(listOf("public"), suggestions);
    }

    @Test
    fun GivenEmptyTrie_WhenInsertingMoreThanTenSuggestions_ConfirmSuggestionsAreLimited() {
        val suggestions = (0..20).map { it.toString() };
        trie.insert("pub", suggestions.subList(0, 10));
        trie.insert("pub", suggestions.subList(10, 20));

        val storedSuggestions = trie.getSuggestions("pub");
        println(trie.printTrie());
        assertTrue(storedSuggestions.size == 10);
    }

    @Test
    fun GivenEmptyTrie_WhenInsertingEmptySuggestions_ConfirmResultsAreCorrect() {
        trie.insert("publ", emptyList());

        val suggestions = trie.getSuggestions("publ");
        assertTrue(suggestions.isEmpty());
    }
}