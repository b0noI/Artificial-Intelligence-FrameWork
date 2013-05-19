package com.aif.common.db;

import com.aif.model.memory.long_time.LongTimeMemoryWord;
import com.aif.model.memory.long_time.LongTimeMemoryWordsConnection;
import org.apache.derby.jdbc.EmbeddedDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * Created with IntelliJ IDEA.
 * User: o.kozlovskiy
 * Date: 1/17/13
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class DBHelper {

    private static DBSettings settings = DBSettings.getSettings(DBSettings.xmlPath);

    private static final String DB_NAME = settings.DB_NAME;

    private static final String WORDS_TABLE_NAME = settings.WORDS_TABLE_NAME;
    private static final String WORD_FIELD = settings.WORD_FIELD;
    private static final String WORD_WEIGHT_FIELD = settings.WORD_WEIGHT_FIELD;
    private static final String WORD_ABSTRACTION_LEVEL = settings.WORD_ABSTRACTION_LEVEL;

    private static final String WORDFORM_TABLE_NAME = settings.WORDFORM_TABLE_NAME;
    private static final String ORIGINAL_WORD_FIELD = settings.ORIGINAL_WORD_FIELD;
    private static final String WORDFORM_FIELD = settings.WORDFORM_FIELD;

    private static final String CONNECTIONS_TABLE_NAME = settings.CONNECTIONS_TABLE_NAME;
    private static final String FIRST_WORD_FIELD = settings.FIRST_WORD_FIELD;
    private static final String SECOND_WORD_FIELD = settings.SECOND_WORD_FIELD;
    private static final String CONNECTION_DISTANCE = settings.CONNECTION_DISTANCE;
    private static final String CONNECTION_PROBABILITY = settings.CONNECTION_PROBABILITY;


    private static final EmbeddedDataSource EMBEDDED_DATA_SOURCE = new EmbeddedDataSource();


    private DBHelper(){
        EMBEDDED_DATA_SOURCE.setDatabaseName(DBHelper.DB_NAME+";create=true;");
    }

    public static DBHelper getInstance(){
        return InstanseHendler.getInstance();
    }

    public void initialize(){
        createWordTable();
        createWordFormsTable();
        createWordConnectionsTable();

    }

    //CREATING TABLES BLOCK
    public void createWordTable(){
        String createQuery =
                "create table " +DBHelper.DB_NAME+ "."+DBHelper.WORDS_TABLE_NAME+
                " ("+DBHelper.WORD_FIELD+" varchar(25) NOT NULL,"+
                " "+DBHelper.WORD_WEIGHT_FIELD+" double NOT NULL,"+
                " "+DBHelper.WORD_ABSTRACTION_LEVEL+" double NOT NULL)";

        try(Statement stm = EMBEDDED_DATA_SOURCE.getConnection().createStatement()){
            stm.execute(createQuery);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createWordFormsTable(){
        String createQuery =
                "create table " +DBHelper.DB_NAME+ "."+DBHelper.WORDFORM_TABLE_NAME+
                        " ("+DBHelper.ORIGINAL_WORD_FIELD+" varchar(25) NOT NULL,"+
                        " "+DBHelper.WORDFORM_FIELD+" varchar(25) NOT NULL)";

        try(Statement stm = EMBEDDED_DATA_SOURCE.getConnection().createStatement()){
            stm.execute(createQuery);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public void createWordConnectionsTable(){
        String createQuery =
                "create table " +DBHelper.DB_NAME+"."+DBHelper.CONNECTIONS_TABLE_NAME+
                " ("+DBHelper.FIRST_WORD_FIELD+" varchar(25) NOT NULL,"+
                " "+DBHelper.SECOND_WORD_FIELD+" varchar(25) NOT NULL,"+
                " "+DBHelper.CONNECTION_DISTANCE+" double NOT NULL,"+
                " "+DBHelper.CONNECTION_PROBABILITY+" double NOT NULL)";

        try(Statement stm = EMBEDDED_DATA_SOURCE.getConnection().createStatement()){
            stm.execute(createQuery);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //PUBLIC METHODS
    public boolean updateWord(LongTimeMemoryWord word){

        if(word == null) return false;

        LongTimeMemoryWord wordInMemory = getWord(word.originalWord);
        if(wordInMemory == null){
            return inputWordToDB(word);
        }

        return updateWord(wordInMemory, word);

    }

    public LongTimeMemoryWord getWord(String word){

        String getQuery = "select * from "+DBHelper.DB_NAME+"."+DBHelper.WORDS_TABLE_NAME+
                " where "+DBHelper.WORD_FIELD+"='"+word+"'";

        try(Statement stm = EMBEDDED_DATA_SOURCE.getConnection().createStatement()){

            ResultSet rs = stm.executeQuery(getQuery);
            if(rs.next()){
                LongTimeMemoryWord out = new LongTimeMemoryWord();

                out.originalWord = rs.getString(DBHelper.WORD_FIELD);
                out.wordWeight = rs.getDouble(DBHelper.WORD_WEIGHT_FIELD);
                out.abstractionLevel = rs.getDouble(DBHelper.WORD_ABSTRACTION_LEVEL);
                out.wordForms = getWordForms(word, stm);

                return out;
            }

            return null;
        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateWordConnection(LongTimeMemoryWordsConnection connection){

        LongTimeMemoryWordsConnection connectionInMemory = getConnection( connection.wordFirst.originalWord, connection.wordSecond.originalWord);
        String updateQuery =
                "update "+DBHelper.DB_NAME+"."+DBHelper.CONNECTIONS_TABLE_NAME+
                " set "+DBHelper.CONNECTION_DISTANCE+" = "+connection.connectionDistance+","
                +DBHelper.CONNECTION_PROBABILITY+" = "+connection.connectionWeight +
                " where "+DBHelper.FIRST_WORD_FIELD+" = '"+connection.wordFirst.originalWord+"' and "
                +DBHelper.SECOND_WORD_FIELD+" = '"+connection.wordSecond.originalWord+"'";

        if(connectionInMemory == null){
            return inputNewConnectiontoDB(connection);
        }else{
            try(Connection conn = EMBEDDED_DATA_SOURCE.getConnection()){

                conn.setAutoCommit(false);

                Statement stm = conn.createStatement();
                stm.executeUpdate(updateQuery);

                conn.commit();
                conn.setAutoCommit(true);
                return true;

            }catch (SQLException e){
                e.printStackTrace();
                return false;
            }
        }
    }

    public LongTimeMemoryWordsConnection getConnection(String word1, String word2){

        LongTimeMemoryWordsConnection out = new LongTimeMemoryWordsConnection();
        out.wordFirst = getWord(word1);
        out.wordSecond = getWord(word2);

        String getQuery = "select * from "+DBHelper.DB_NAME+"."+DBHelper.CONNECTIONS_TABLE_NAME+
                " where "+DBHelper.FIRST_WORD_FIELD+" = '"+word1+"' and "+DBHelper.SECOND_WORD_FIELD+" = '"+word2+"'";

        try(Statement stm = EMBEDDED_DATA_SOURCE.getConnection().createStatement()){

            ResultSet rs = stm.executeQuery(getQuery);
            if(rs.next()){
                out.connectionDistance = rs.getDouble(DBHelper.CONNECTION_DISTANCE);
                out.connectionWeight = rs.getDouble(DBHelper.CONNECTION_PROBABILITY);
                return out;

            }
            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

//    public static boolean check(String word, Set<String> wordForms){
//
//        EMBEDDED_DATA_SOURCE.setDatabaseName(DBHelper.DB_NAME);
//
//        String getQuery = " select * from "+DBHelper.DB_NAME+"."+DBHelper.WORDFORM_TABLE_NAME+
//                        " where "+DBHelper.WORD_FIELD+" = '"+word+"'";
//
//        try(Statement stm = EMBEDDED_DATA_SOURCE.getConnection().createStatement()){
//
//            ResultSet rs = stm.executeQuery(getQuery);
//
//            if (rs.next() && rs.getString(DBHelper.WORD_FIELD).equals(word)){
//                return true;
//            }
//
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//
//        Iterator it = wordForms.iterator();
//        while(it.hasNext()){
//
//            String wordToCheck = it.next().toString();
//
//            getQuery = " select * from "+DBHelper.DB_NAME+"."+DBHelper.WORDFORM_TABLE_NAME+
//                    " where "+DBHelper.WORD_FIELD+" = '"+wordToCheck+"'";
//
//            try(Statement stm = EMBEDDED_DATA_SOURCE.getConnection().createStatement()){
//
//                ResultSet rs = stm.executeQuery(getQuery);
//
//                if(rs.next() && rs.getString(DBHelper.WORD_FIELD).equals(wordToCheck)){
//                    return true;
//                }
//
//            }catch(SQLException e){
//                e.printStackTrace();
//            }
//        }
//
//        return false;
//    }


    //DBLoader methods
    protected String findWordInWordForms(String word){

        String getQuery = "select * from "+DBHelper.DB_NAME+"."+DBHelper.WORDFORM_TABLE_NAME+
                " where "+DBHelper.WORDFORM_FIELD+" = '"+word+"'";

        try(Statement stm = EMBEDDED_DATA_SOURCE.getConnection().createStatement()){

            ResultSet rs = stm.executeQuery(getQuery);

            if(rs.next()){
                return rs.getString(DBHelper.ORIGINAL_WORD_FIELD);
            }

            return null;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    protected Set<String> getWordsSet(){

        Set<String> out = new HashSet<String>();

        String getQuery = "select "+DBHelper.WORD_FIELD+" from "+DBHelper.DB_NAME+"."+DBHelper.WORDS_TABLE_NAME;

        try(Statement stm = EMBEDDED_DATA_SOURCE.getConnection().createStatement()){

            ResultSet rs = stm.executeQuery(getQuery);
            while(rs.next()){
                out.add(rs.getString(DBHelper.WORD_FIELD));
            }

            return out;

        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }


    // PRIVATE METHODS
    private boolean updateWord(LongTimeMemoryWord oldWord, LongTimeMemoryWord newWord){

        try(Connection conn = EMBEDDED_DATA_SOURCE.getConnection()){

            conn.setAutoCommit(false);

            Statement stm = conn.createStatement();
            Set<String> oldWordForms = oldWord.wordForms;
            Set<String> newWordForms = newWord.wordForms;

            for(String newWordForm: newWordForms){
                if(!oldWordForms.contains(newWordForm)){
                    updateWordForms(oldWord.originalWord, newWordForm, stm);
                }
            }

            ResultSet rs = stm.executeQuery("select * from "+DBHelper.DB_NAME+"."+DBHelper.WORDFORM_TABLE_NAME+
                    " where "+DBHelper.WORD_FIELD+" = '"+oldWord);
            int ID = rs.getInt("WORD_ID");

            oldWord.merge(newWord);

            String updateQuery = "update "+DBHelper.DB_NAME+"."+DBHelper.WORDS_TABLE_NAME+
                    " set "+DBHelper.WORD_WEIGHT_FIELD+" = "+oldWord.wordWeight+", "
                    +DBHelper.WORD_ABSTRACTION_LEVEL+" = "+oldWord.abstractionLevel+
                    " where "+DBHelper.WORD_FIELD+" = "+oldWord.originalWord+" ";

            stm.executeUpdate(updateQuery);

            conn.commit();
            conn.setAutoCommit(true);
            return true;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    private void updateWordForms(String word, String WordForm, Statement stm){

        String queryString = "insert into "+DBHelper.DB_NAME+"."+DBHelper.WORDFORM_TABLE_NAME+
                " ("+DBHelper.ORIGINAL_WORD_FIELD+", "+DBHelper.WORDFORM_FIELD+")"+
                " values ('"+word+"', '"+WordForm+"')";

        try {

            stm.executeUpdate(queryString);

        }catch ( SQLException e){
            e.printStackTrace();
        }
    }

    private boolean inputWordToDB(LongTimeMemoryWord word){

        if(word.originalWord == null) return false;

        String wordQuery = "insert into "+ DBHelper.DB_NAME+"."+DBHelper.WORDS_TABLE_NAME+
                           " ("+DBHelper.WORD_FIELD+", "+DBHelper.WORD_WEIGHT_FIELD+", "+DBHelper.WORD_ABSTRACTION_LEVEL+")"+
                           " values ('"+word.originalWord+"', "+word.wordWeight+", "+word.abstractionLevel+")";

        try (Connection conn = EMBEDDED_DATA_SOURCE.getConnection()){

            conn.setAutoCommit(false);
            Statement stm = conn.createStatement();

            stm.executeUpdate(wordQuery);
            inputWordForms(word, stm);

            conn.commit();
            conn.setAutoCommit(true);
            return true;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }


    private void inputWordForms(LongTimeMemoryWord word, Statement stm){
        String wordformQuery = "insert into "+DBHelper.DB_NAME+"."+DBHelper.WORDFORM_TABLE_NAME+
                " ("+DBHelper.ORIGINAL_WORD_FIELD+", "+DBHelper.WORDFORM_FIELD+")"+
                " values ('"+word.originalWord+"', ";

        try{

            Iterator it = word.wordForms.iterator();
            while(it.hasNext()){
                stm.executeUpdate(wordformQuery + "'" + it.next() + "')");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }


    }


    private boolean inputNewConnectiontoDB(LongTimeMemoryWordsConnection connection){

        String connectionQuery = "insert into "+DBHelper.DB_NAME+"."+DBHelper.CONNECTIONS_TABLE_NAME+
                " ("+DBHelper.FIRST_WORD_FIELD+", "+DBHelper.SECOND_WORD_FIELD+", "+DBHelper.CONNECTION_DISTANCE+", "+DBHelper.CONNECTION_PROBABILITY+")"+
                " values ('"+connection.wordFirst.originalWord+"', '"+connection.wordSecond.originalWord+"',"
                +connection.connectionDistance+", "+connection.connectionWeight +")";

        if(!(connection.connectionWeight <= 1 && connection.connectionWeight >= 0)){
            return false;
        }

        try(Connection conn = EMBEDDED_DATA_SOURCE.getConnection()){
            conn.setAutoCommit(false);

            Statement stm = conn.createStatement();
            stm.executeUpdate(connectionQuery);

            conn.commit();
            conn.setAutoCommit(true);
            return true;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    private Set<String> getWordForms(String word, Statement stm){

        Set<String> out = new HashSet();
        String getQuery =  "select * from "+DBHelper.DB_NAME+"."+DBHelper.WORDFORM_TABLE_NAME+
                " where "+DBHelper.ORIGINAL_WORD_FIELD+"='"+word+"'";

        try{

            ResultSet rs = stm.executeQuery(getQuery);

            while(rs.next()){
                if(rs.getString(DBHelper.WORDFORM_FIELD) != null){
                    out.add(rs.getString(DBHelper.WORDFORM_FIELD));
                }
            }

            return out;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }



    // METHODS FOR TESTS Should be removed after testing

    public void dropWordformsTable(){

        String dropQuery = "drop table "+DBHelper.DB_NAME+
                "."+DBHelper.WORDFORM_TABLE_NAME;

        try(Statement stm = EMBEDDED_DATA_SOURCE.getConnection().createStatement()){
            stm.execute(dropQuery);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void dropWordTable(){

        String dropQuery = "drop table "+DBHelper.DB_NAME+
                           "."+DBHelper.WORDS_TABLE_NAME;

        try(Statement stm = EMBEDDED_DATA_SOURCE.getConnection().createStatement()){
            stm.execute(dropQuery);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void dropConnectionsTable(){

        String dropQuery = "drop table "+DBHelper.DB_NAME+
                "."+DBHelper.CONNECTIONS_TABLE_NAME;

        try(Statement stm = EMBEDDED_DATA_SOURCE.getConnection().createStatement()){
            stm.execute(dropQuery);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static class InstanseHendler{

        private static final DBHelper INSTANCE = new DBHelper();

        static DBHelper getInstance(){
            return InstanseHendler.INSTANCE;
        }
    }
}


