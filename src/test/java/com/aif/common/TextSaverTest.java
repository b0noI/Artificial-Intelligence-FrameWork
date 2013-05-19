package com.aif.common;

import com.aif.model.memory.short_time.Text;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * User: b0noI
 * Date: 23.12.12
 * Time: 13:36
 */
public class TextSaverTest {

    private static final String TEXTS_DIR = "src/test/texts/";

    private static final String FILE_NAME = TEXTS_DIR + "test.txt";

    private Text text;

    @Before
    public void setUp() throws Exception {
        text = new Text(FILE_NAME);
    }

    @Test
    public void testSaveTextDataToDB() throws Exception {
        TextSaver textSaver = new TextSaver(text);
        textSaver.saveTextDataToDB();
        TestCase.assertNotNull(textSaver);
    }

}
