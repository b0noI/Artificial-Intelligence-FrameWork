
import com.aif.model.memory.short_time.Text;
import com.aif.model.memory.short_time.WordsStorage;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TextTest {

    private static final String FILE_NAME = TextTest.TEXTS_DIR + "test.txt";

    private static final String TEXTS_DIR = "src/test/texts/";

    private static final String HARD_TEXT_FILE_NAME = TextTest.TEXTS_DIR  + "hardTest.txt";

    private static final String FILE_TEXT = "The class String includes methods for examining individual characters of the sequence, for comparing strings, for searching strings, for extracting substrings, and for creating a copy of a string with all characters translated to uppercase or to lowercase. Case mapping relies heavily on the information provided by the Unicode Consortium's Unicode 3.0 specification. The specification's UnicodeData.txt and SpecialCasing.txt files are used extensively to provide case mapping. ";

    private static Text sText;

    @Before
    public void setUp() throws Exception {
         writeTestText();
    }

    @Test
    public void testCreatingTextObject() throws Exception{
        Text text = new Text(FILE_NAME);
        TestCase.assertNotSame(text.getSentencesCount(), 0);
    }

    @Test
    public void testHardTextOpen() throws Exception{
        WordsStorage wordsStorage = new WordsStorage();
        wordsStorage.clearStorage();
        Text text = new Text(HARD_TEXT_FILE_NAME);
        TestCase.assertNotSame(text.getSentencesCount(), 0);
        TestCase.assertNotSame(wordsStorage.getWordsCount(), 0);
        text.getWords();
        sText = text;
    }

    @Test
    public void testGetThem() throws Exception{
        TestCase.assertNotSame(sText.getThem().size(), 0);
    }

    @Test
    public void testGetObjects() throws Exception{
        TestCase.assertNotSame(sText.getObjects().size(), 0);
    }

//    Book test too long!
//    @Test
//    public void testBookOpen() throws Exception{
//        WordsStorage wordsStorage = new WordsStorage();
//        wordsStorage.clearStorage();
//        Text text = new Text(BOOK_TEXT_FILE_NAME);
//        TestCase.assertNotSame(text.getSentencesCount(), 0);
//        TestCase.assertNotSame(wordsStorage.getWordsCount(), 0);
//        sText = text;
//    }
//
//    @Test
//    public void testSortMostWeightWords() throws Exception{
//        List<Word> sortedWords = sText.getWords();
//        TestCase.assertNotSame(sortedWords.size(), 0);
//    }
//
//    @Test
//    public void testGetBookThem() throws Exception{
//        TestCase.assertNotSame(sText.getThem().size(), 0);
//    }

    private void writeTestText(){
        try (PrintWriter out =
                     new PrintWriter(new FileWriter(FILE_NAME))){
            out.println(FILE_TEXT);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
