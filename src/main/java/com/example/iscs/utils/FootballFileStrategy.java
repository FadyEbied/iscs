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
public class FootballFileStrategy implements FileStrategy {

    public static String FILETYPE = "Football File";
    private static String[] HEADERS ={"Team","F","A"};
    private static Integer MAX_VALUE=99;


    private List<IndexesHeader> indexesHeaders;
    private List<String> ignoreRows = new ArrayList<>();
    private Property property;
    private int scored;
    private int suffered;

    @Override
    public void determineIndexes(String line){
        indexesHeaders = Functions.DETERMINE_INDEXES(line, HEADERS);
    }

    @Override
    public void doOperation(BufferedReader reader) throws IOException {
        String line;
        String key="";
        int value=MAX_VALUE;

        int indexKey= indexesHeaders.stream().filter(c->c.getKey().equals(HEADERS[0])).findFirst().get().getIndex()+1;
        int indexValueF= indexesHeaders.stream().filter(c->c.getKey().equals(HEADERS[1])).findFirst().get().getIndex()+1;
        int indexValueA= indexesHeaders.stream().filter(c->c.getKey().equals(HEADERS[2])).findFirst().get().getIndex()+2;

        int diff=0;
        int scored;
        int suffered;
        while ((line=reader.readLine())!=null){
            String[] row = line.trim().split("\\s+");
            if (row.length>1) {
                try{
                    scored = Integer.parseInt(row[indexValueF]);
                    suffered = Integer.parseInt(row[indexValueA]);
                    if (scored > suffered)
                        diff = scored -suffered;
                    else
                        diff = suffered - scored;
                   if (diff<value) {
                       key = row[indexKey];
                       value = diff;
                   }

                }catch (RuntimeException ex){
                    ignoreRows.add("Row format is wrong: "+line);
                }
            }
        }
        property = new Property(key,value+"");

    }
}
