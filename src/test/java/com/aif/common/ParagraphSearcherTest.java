package com.aif.common;

import com.aif.model.memory.short_time.Sentence;
import com.aif.model.memory.short_time.Text;
import com.aif.model.memory.short_time.WordsStorage;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

/**
 * User: b0noI
 * Date: 01.02.13
 * Time: 17:19
 */
public class ParagraphSearcherTest {

    private static final String TEXTS_DIR = "src/test/texts/";

    private static final String HARD_TEXT_FILE_NAME = TEXTS_DIR  + "hardTest.txt";

    private static final String BOOK_TEXT_FILE_NAME = TEXTS_DIR + "book.txt";

    private Text text;

    private WordsStorage wordsStorage;

    @Test
    public void testFindParagraph() throws Exception {
        wordsStorage = new WordsStorage();
        wordsStorage.clearStorage();
        text = new Text(HARD_TEXT_FILE_NAME);
        List<Sentence> paragraph = ParagraphSearcher.findParagraph(text, 5);
        TestCase.assertNotSame(paragraph.size(), 0);
    }

    // Long test !
    @Test
    public void testFindParagraphOnBook() throws Exception {
        wordsStorage = new WordsStorage();
        wordsStorage.clearStorage();
        text = new Text(BOOK_TEXT_FILE_NAME);
        List<Sentence> paragraph = ParagraphSearcher.findParagraph(text, 5);
        TestCase.assertNotSame(paragraph.size(), 0);
    }

}
