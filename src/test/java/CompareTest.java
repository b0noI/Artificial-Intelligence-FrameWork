
import com.aif.common.WordComparator;
import com.aif.model.memory.short_time.Word;
import com.aif.model.memory.short_time.WordsStorage;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class CompareTest {

    private Word word1 = null;

    private Word word2 = null;

    private Word word3 = null;

    private Word word4 = null;

    private Word word5 = null;

    private Word word6 = null;

    private Word word7 = null;

    private WordComparator wordComparatorTest = null;

    @Before
    public void setUp() throws Exception {

        WordsStorage wordsStorage = new WordsStorage();
        word1 = wordsStorage.generateWordFromString("aabbcc");
        wordsStorage.clearStorage();
        word2 = wordsStorage.generateWordFromString("vvbbgg");
        wordsStorage.clearStorage();
        word3 = wordsStorage.generateWordFromString("ddgggb");
        wordsStorage.clearStorage();
        word4 = wordsStorage.generateWordFromString("aabbcc");
        wordsStorage.clearStorage();
        word5 = wordsStorage.generateWordFromString("aabkkk");
        wordsStorage.clearStorage();
        word6 = wordsStorage.generateWordFromString("aaccbbe");
        wordsStorage.clearStorage();
        word7 = wordsStorage.generateWordFromString("gaarrbbu");
        wordComparatorTest = WordComparator.getInstance();

    }

    @Test
    public void testLongestSubstring() throws Exception {
        assertEquals(wordComparatorTest.longestSubstring(word1, word2), "bb");
        assertEquals(wordComparatorTest.longestSubstring(word2, word3), "gg");

    }

    @Test
    public void testDistance() throws Exception {
        assertEquals(wordComparatorTest.recursiveDistanceBetweenWords(word1, word4), 1.0d);
        assertEquals(wordComparatorTest.recursiveDistanceBetweenWords(word6, word7), (8d / 15d));

    }

    @Test
    public void testSymbolDistance() throws Exception {
        assertEquals(wordComparatorTest.symbolDistanceBetweenWords(word5, word4), (6d / 12d));
        assertEquals(wordComparatorTest.recursiveDistanceBetweenWords(word6, word7), (8d / 15d));

    }
}
