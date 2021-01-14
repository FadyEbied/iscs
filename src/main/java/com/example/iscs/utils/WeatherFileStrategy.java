package com.example.iscs.utils;

import com.example.iscs.model.IndexesHeader;
import com.example.iscs.model.Property;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Getter
public class WeatherFileStrategy implements FileStrategy {

    public static String FILETYPE = "Weather File";
    private static String[] HEADERS ={"Dy","MnT"};
    private static Integer MAX_VALUE=99;

    private List<IndexesHeader> indexesHeader;
    private List<String> ignoreRows = new ArrayList<>();
    private Property property;

    @Override
    public void determineIndexes(String line){
        indexesHeader = Functions.DETERMINE_INDEXES(line, HEADERS);
    }

    @Override
    public void doOperation(BufferedReader reader) throws IOException {
        String line;
        String key="";
        int value=MAX_VALUE;
        int indexKey= indexesHeader.stream().filter(c->c.getKey().equals(HEADERS[0])).findFirst().get().getIndex();
        int indexValue= indexesHeader.stream().filter(c->c.getKey().equals(HEADERS[1])).findFirst().get().getIndex();

        while ((line=reader.readLine())!=null){
            String[] row = line.trim().split("\\s+");
            if (row.length>1) {
                try{
                    if (Integer.parseInt(row[indexValue]) < value) {
                        value = Integer.parseInt(row[indexValue]);
                        key = row[indexKey];
                    }
                }catch (RuntimeException ex){
                    ignoreRows.add("Row format is wrong: "+line);
                }
            }
        }
        property = new Property(key,value+"");
    }
}
