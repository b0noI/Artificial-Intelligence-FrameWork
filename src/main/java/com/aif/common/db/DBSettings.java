package com.aif.common.db;

import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: o.kozlovskiy
 * Date: 1/29/13
 * Time: 6:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBSettings {

    public static final String xmlPath = "xml/DBSettings.xml";

    public String DB_NAME;

    public String WORDS_TABLE_NAME;
    public String WORD_FIELD;
    public String WORD_WEIGHT_FIELD;
    public String WORD_ABSTRACTION_LEVEL;

    public String WORDFORM_TABLE_NAME;
    public String ORIGINAL_WORD_FIELD;
    public String WORDFORM_FIELD;

    public String CONNECTIONS_TABLE_NAME;
    public String FIRST_WORD_FIELD;
    public String SECOND_WORD_FIELD;
    public String CONNECTION_DISTANCE;
    public String CONNECTION_PROBABILITY;

    private DBSettings(){

    }

    public static DBSettings getSettings(String xmlPath){
        XStream xstream = new XStream();
        xstream.alias("settings", DBSettings.class);
        String xml = "";
        try{
            Scanner sc = new Scanner(new File(xmlPath));
            while(sc.hasNext()){
                xml = xml + sc.nextLine()+ "\n";
            }

            if(xml != null){
                return (DBSettings)xstream.fromXML(xml);
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
}
