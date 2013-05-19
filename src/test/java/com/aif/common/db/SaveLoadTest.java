package com.aif.common.db;

import com.aif.model.memory.long_time.LongTimeMemoryWord;
import com.aif.model.memory.short_time.Text;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

/**
 * Created with IntelliJ IDEA.
 * User: o.kozlovskiy
 * Date: 1/30/13
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class SaveLoadTest {

    private DBHelper dbHelper = DBHelper.getInstance();
    private DBLoader dbLoader = DBLoader.getInstance();
    private DBSaver dbSaver;

    private static final String TEXTS_DIR = "src/test/texts/";
    private static final String FILE_NAME = TEXTS_DIR + "test.txt";
    private Text text;

    @Before
    public void setUp() throws Exception{

        dropDown();

        text = new Text(FILE_NAME);
        dbHelper.createWordTable();
        dbHelper.createWordFormsTable();
        dbHelper.createWordConnectionsTable();

    }

    @After
    public void dropDown(){

        text = null;
        dbHelper.dropWordTable();
        dbHelper.dropWordformsTable();
        dbHelper.dropConnectionsTable();


    }

    @Test
    public void saveLoadTest(){

        dbSaver = new DBSaver(text);
        dbSaver.save();

        String outWord = dbLoader.findWordInWOrdforms("class");

        System.out.println(outWord);
        assertNotNull(outWord);

    }

    @Test
    public void getWordSetFromDB(){

        dbSaver = new DBSaver(text);
        dbSaver.save();

        Set<String> outSet = new HashSet<String>();

        outSet = dbLoader.getWordSet();

        Iterator it = outSet.iterator();
        while (it.hasNext()){
            System.out.println(it.next());
        }

        assertNotSame(0,  outSet.size());

    }

    @Test
    public void getConnectionTest(){

        dbSaver = new DBSaver(text);
        dbSaver.save();

        dbLoader.setWord("case");
        String originalWord = dbLoader.getOriginalWord();
        double connectionDistance = dbLoader.getDistanseToWord("examining ");
        double connectionProbability = dbLoader.getConnectionProbabilityWithWord("examining ");

        System.out.println("Original word is "+originalWord+", and connection distance to heavily = "+connectionDistance+", connection probability = "+connectionProbability);

        assertNotSame(0.0, connectionDistance);
    }

}
