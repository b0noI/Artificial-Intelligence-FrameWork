
import com.aif.model.memory.short_time.Word;
import com.aif.model.memory.short_time.WordsStorage;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WordTest {

    private String strForTest = null;

    @Before
    public void setUp() throws Exception {
         strForTest = new String("test ");
    }

    @Test
    public void testToString() throws Exception {
        Word testWord = new WordsStorage().generateWordFromString(strForTest);
        assertTrue(testWord.toString().equals("test"));
        assertFalse(testWord.toString().equals("TEST"));
        assertFalse(testWord.toString().equals("test "));
    }

    @Test
    public void testEquals() throws Exception {
        WordsStorage wordsStorage = new WordsStorage();
        Word testWord1 = wordsStorage.generateWordFromString("test1");
        wordsStorage.clearStorage();
        Word testWord2 = wordsStorage.generateWordFromString("te2st12");
        wordsStorage.clearStorage();
        Word testWord3 = wordsStorage.generateWordFromString("sdfsdg");
        wordsStorage.clearStorage();

        assertTrue(testWord1.equals(testWord1));
        assertFalse(testWord1.equals(testWord3));
    }
}
