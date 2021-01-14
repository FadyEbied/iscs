package com.example.iscs.utils;

import com.example.iscs.model.IndexesHeader;

import java.util.ArrayList;
import java.util.List;

public class Functions {


    public static List<IndexesHeader> DETERMINE_INDEXES(String line, String[] headers){

        List<IndexesHeader> indexesHeaders = new ArrayList<>();

        String[] fileHeaders = line.trim().split("\\s+");
        for (int i = 0; i < fileHeaders.length; i++) {
            for (String value: headers) {
                if (fileHeaders[i].equals(value)) {
                    indexesHeaders.add(new IndexesHeader(value,i));
                    break;
                }
            }
        }
        return indexesHeaders;
    }
}
