package sample.controllers;

import sample.util.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DataController {


    public ArrayList<String[]> getData(String path) {
        ArrayList<String[]> data = new ArrayList<>();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            br.readLine();
            for(int i = 0; i < Constants.MAX_PRODUCTS; i++){
                if ((line = br.readLine()) != null) {
                    String[] values = line.split(", ");
                    data.add(values);
                }
            }
            br.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}