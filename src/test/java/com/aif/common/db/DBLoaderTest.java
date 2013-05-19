package com.aif.common.db;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Created with IntelliJ IDEA.
 * User: o.kozlovskiy
 * Date: 1/24/13
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBLoaderTest {

    private DBHelper DBHelper;
    private DBLoader DBLoader;

    @BeforeClass
    public static void beforeAll(){

    }


    @Before
    public void SetUpTest(){

        DBHelper = com.aif.common.db.DBHelper.getInstance();
        DBLoader = com.aif.common.db.DBLoader.getInstance();
    }

    @After
    public void DropDownTestData(){

        DBHelper = null;
        DBLoader = null;
    }

    @Test
    public void testFindWordInWordForms(){

    }


    //Non-test function
    private void InitializeDB(){}
}
