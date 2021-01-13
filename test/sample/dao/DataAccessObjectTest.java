package sample.dao;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessObjectTest {
    private String invalidPath = "invalid path to file";

    @Test
    void loadDataInvalidPath(){
        DataAccessObject dao = DataAccessObject.getInstance();
        try{
            dao.loadData(invalidPath);
            fail("Exception not thrown, though passed invalid path");
        }catch(IllegalArgumentException e){
            System.out.println("Exception thrown properly: " + e.getMessage());
        }
    }
}