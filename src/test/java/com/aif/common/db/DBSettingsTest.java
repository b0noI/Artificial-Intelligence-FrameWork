package com.aif.common.db;

import com.thoughtworks.xstream.XStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: o.kozlovskiy
 * Date: 1/29/13
 * Time: 6:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBSettingsTest {

    private DBSettings settings;


    @Before
    public void setUp(){
        settings = DBSettings.getSettings(DBSettings.xmlPath);
    }

    @After
    public void dropDown(){
        settings = null;
    }

    @Test
    public void DB_NAME_Test(){
        assertEquals("wdb", settings.DB_NAME);
    }

    @Test
    public void WORD_TABLE_NAME_Test(){
        assertEquals("WORDS_TABLE", settings.WORDS_TABLE_NAME);
    }

    @Test
    public void WORD_FIELD_Test(){
        assertEquals("WORD", settings.WORD_FIELD);
    }

    @Test
    public void WORD_WEIGHT_FIELD_Test(){
        assertEquals("WEIGHT", settings.WORD_WEIGHT_FIELD);
    }

    @Test
    public void WORD_ABSTRACTION_LEVEL_Test(){
        assertEquals("ABSTRACTION", settings.WORD_ABSTRACTION_LEVEL);
    }

    @Test
    public void WORDFORM_TABLE_NAME_Test(){
        assertEquals("WORDFORMS", settings.WORDFORM_TABLE_NAME);
    }

    @Test
    public void ORIGINAL_WORD_FIELD_Test(){
        assertEquals("WORD", settings.ORIGINAL_WORD_FIELD);
    }

    @Test
    public void WORDFORM_FIELD_Test(){
        assertEquals("WORDFORM", settings.WORDFORM_FIELD);
    }

    @Test
    public void CONNECTIONS_TABLE_NAME_Test(){
        assertEquals("CONNECTIONS", settings.CONNECTIONS_TABLE_NAME);
    }

    @Test
    public void FIRST_WORD_FIELD_Test(){
        assertEquals("FIRST_WORD", settings.FIRST_WORD_FIELD);
    }

    @Test
    public void SECOND_WORD_FIELD_Test(){
        assertEquals("SECOND_WORD", settings.SECOND_WORD_FIELD);
    }

    @Test
    public void CONNECTION_DISTANCE_Test(){
        assertEquals("CONNECTION_DISTANCE", settings.CONNECTION_DISTANCE);
    }

    @Test
    public void CONNECTION_PROBABILITY_Test(){
        assertEquals("CONNECTION_PROBABILITY", settings.CONNECTION_PROBABILITY);
    }
}
