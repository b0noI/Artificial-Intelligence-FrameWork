package com.aif.common.db;

import com.aif.model.memory.long_time.LongTimeMemoryWord;
import com.aif.model.memory.long_time.LongTimeMemoryWordsConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.*;

/**
 * Created with IntelliJ IDEA.
 * User: o.kozlovskiy
 * Date: 1/17/13
 * Time: 11:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class DBHelperTest{

    private DBHelper DBHelper;

    @Before
    public void setUp(){
        DBHelper = com.aif.common.db.DBHelper.getInstance();
        DBHelper.initialize();
    }

    @After
    public void DropDown(){

        DBHelper.dropWordTable();
        DBHelper.dropWordformsTable();
        DBHelper.dropConnectionsTable();

    }

    @Test
    public void testAddNewWorToDB(){

        LongTimeMemoryWord inputWord = new LongTimeMemoryWord();

        inputWord.originalWord = "Hello";
        inputWord.wordWeight = 12.5;
        inputWord.abstractionLevel = 0.7;
        inputWord.wordForms = new HashSet<String>();
        inputWord.wordForms.add("Hi");
        inputWord.wordForms.add("Hrwdy");
        inputWord.wordForms.add("Oven");

        assertEquals(3, inputWord.wordForms.size());

        DBHelper.updateWord(inputWord);

        LongTimeMemoryWord out = DBHelper.getWord("Hello");

        assertEquals("Hello", out.originalWord);
        assertEquals(12.5, out.wordWeight);
        assertEquals(0.7, out.abstractionLevel);

        assertEquals(3, out.wordForms.size());
    }

    @Test
    public void testUpdateExistingWordInDB(){

        LongTimeMemoryWord inputWord = new LongTimeMemoryWord();

        inputWord.originalWord = "Hello";
        inputWord.wordWeight = 2;
        inputWord.abstractionLevel = 2;
        inputWord.wordForms = new HashSet<String>();
        inputWord.wordForms.add("Hi");
        inputWord.wordForms.add("Hrwdy");
        inputWord.wordForms.add("Oven");

        assertEquals(3, inputWord.wordForms.size());

        assertTrue(DBHelper.updateWord(inputWord));

        inputWord.originalWord = "HIk";
        inputWord.abstractionLevel = 4;
        inputWord.wordWeight = 4;
        inputWord.wordForms.add("Plug");
        inputWord.wordForms.add("Share");

        assertTrue(DBHelper.updateWord(inputWord));

        LongTimeMemoryWord out = DBHelper.getWord("HIk");

        assertEquals(4.0, out.wordWeight);
        assertEquals(4.0, out.abstractionLevel);
        assertEquals(5, out.wordForms.size());
        assertEquals("HIk", out.originalWord);


    }

    @Test
    public void testAddNullObjectToDataBase(){

        LongTimeMemoryWord inputWord = null;
        assertFalse(DBHelper.updateWord(inputWord));

    }

    @Test
    public void testAddWordWithNullOriginalWord(){

        LongTimeMemoryWord inputWord = new LongTimeMemoryWord();
        inputWord.abstractionLevel = 12;
        inputWord.wordWeight = 1.4;
        inputWord.wordForms = new HashSet<String>();
        inputWord.wordForms.add("One");
        inputWord.wordForms.add("Two");

        assertFalse(DBHelper.updateWord(inputWord));
    }

    @Test
    public void testAddNewConnectionsToDB(){

        LongTimeMemoryWord w1 = new LongTimeMemoryWord();
        w1.originalWord = "Looper";
        LongTimeMemoryWord w2 = new LongTimeMemoryWord();
        w2.originalWord = "Wombat";
        LongTimeMemoryWordsConnection con = new LongTimeMemoryWordsConnection();

        con.wordFirst = w1;
        con.wordSecond = w2;
        con.connectionDistance = 13.5;
        con.connectionWeight = 0.5;

        DBHelper.updateWordConnection(con);

        LongTimeMemoryWordsConnection out = DBHelper.getConnection("Looper", "Wombat");

        assertEquals(con.connectionDistance, out.connectionDistance);
        assertEquals(con.connectionWeight, out.connectionWeight);
    }

    @Test
    public void testUpdateExistingConnectionToDB(){

        LongTimeMemoryWord w1 = new LongTimeMemoryWord();
        w1.originalWord = "Looper";
        LongTimeMemoryWord w2 = new LongTimeMemoryWord();
        w2.originalWord = "Wombat";
        LongTimeMemoryWordsConnection con = new LongTimeMemoryWordsConnection();

        con.wordFirst = w1;
        con.wordSecond = w2;
        con.connectionDistance = 13.5;
        con.connectionWeight = 0.5;

        DBHelper.updateWordConnection(con);

        con.connectionDistance = 10.65;
        con.connectionWeight = 0.7;

        DBHelper.updateWordConnection(con);

        LongTimeMemoryWordsConnection out = DBHelper.getConnection("Looper", "Wombat");

        assertEquals(10.65, out.connectionDistance);
        assertEquals(0.7, out.connectionWeight);
    }

    @Test
    public void testGetWordByWordform(){

        LongTimeMemoryWord inputWord = new LongTimeMemoryWord();

        inputWord.originalWord = "Hello";
        inputWord.wordWeight = 2;
        inputWord.abstractionLevel = 2;
        inputWord.wordForms = new HashSet<String>();
        inputWord.wordForms.add("Hi");
        inputWord.wordForms.add("Hrwdy");
        inputWord.wordForms.add("Oven");

        DBHelper.updateWord(inputWord);

        String out = DBHelper.findWordInWordForms("Oven");

        assertEquals("Hello", out);
    }

    @Test
    public void testGetWordsSet(){

        Set<String> check = new HashSet<String>();
        Set<String> out = new HashSet<String>();

        LongTimeMemoryWord inputWord = new LongTimeMemoryWord();

        inputWord.originalWord = "Hello";
        inputWord.wordWeight = 2;
        inputWord.abstractionLevel = 2;
        inputWord.wordForms = new HashSet<String>();
        inputWord.wordForms.add("Hi");
        inputWord.wordForms.add("Hrwdy");
        inputWord.wordForms.add("Oven");

        check.add(inputWord.originalWord);

        DBHelper.updateWord(inputWord);

        LongTimeMemoryWord inputWord2 = new LongTimeMemoryWord();

        inputWord2.originalWord = "HelBo";
        inputWord2.wordWeight = 7;
        inputWord2.abstractionLevel = 5;
        inputWord2.wordForms = new HashSet<String>();
        inputWord2.wordForms.add("Hi2");
        inputWord2.wordForms.add("Hrdy");
        inputWord2.wordForms.add("Ovewr");

        DBHelper.updateWord(inputWord2);

        check.add(inputWord2.originalWord);

        out = DBHelper.getWordsSet();

        assertEquals(check, out);
    }
}
