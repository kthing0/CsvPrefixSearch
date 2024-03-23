package collection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVParser {
    private String filePath;

    public CSVParser(String filePath) {
        this.filePath = filePath;
    }

    public BufferedReader getBufferedReader() throws IOException {
        return new BufferedReader(new FileReader(filePath));
    }
}

