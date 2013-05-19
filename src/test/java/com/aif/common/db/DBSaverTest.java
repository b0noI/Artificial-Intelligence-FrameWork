package com.aif.common.db;

import com.aif.model.memory.short_time.Text;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: o.kozlovskiy
 * Date: 1/30/13
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBSaverTest {

    private static final String TEXTS_DIR = "src/test/texts/";
    private static final String FILE_NAME = TEXTS_DIR + "test.txt";
    private Text text;
    private DBSaver dbSaver;
    private DBHelper dbHelper = DBHelper.getInstance();

    @Before
    public void setUp() throws Exception {

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
    public void saveTest(){

        dbSaver = new DBSaver(text);
        dbSaver.save();

        assertNotNull(dbSaver);


    }
}
