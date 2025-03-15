import org.junit.Test
import kotlin.test.*;

class TestTrie {

    @Test
    fun GivenEmptyTrie_WhenDoNothing_ConfirmEmpty() {
        val trie = Trie();
        assertTrue(trie.getSize() == 0);
    }

    @Test
    fun GivenEmptyTrie_WhenInsertingChars_ConfirmIsCorrect() {
        val trie = Trie();
        trie.insert("static", listOf("static"));
        trie.insert("public", listOf("public"));

        assertTrue(trie.getSize() == 13);

        println(trie.printTrie());
    }
}