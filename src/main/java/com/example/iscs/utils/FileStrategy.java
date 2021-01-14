package com.example.iscs.utils;

import java.io.BufferedReader;
import java.io.IOException;

public interface FileStrategy {
    void doOperation(final BufferedReader reader) throws IOException;
    void determineIndexes(String line);
}
