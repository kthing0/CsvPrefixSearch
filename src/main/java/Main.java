import collection.CSVParser;
import collection.CSVSearch;
import com.fasterxml.jackson.databind.ObjectMapper;
import utils.OutputJson;
import utils.SearchResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            String dataPath = null;
            int indexedColumnId = -1;
            String inputFilePath = null;
            String outputFilePath = null;

            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--data":
                        dataPath = args[++i];
                        break;
                    case "--indexed-column-id":
                        indexedColumnId = Integer.parseInt(args[++i]) - 1;
                        break;
                    case "--input-file":
                        inputFilePath = args[++i];
                        break;
                    case "--output-file":
                        outputFilePath = args[++i];
                        break;
                    default:
                        throw new IllegalArgumentException("Parameter not found: " + args[i]);
                }
            }

            if (dataPath == null || indexedColumnId == -1 || inputFilePath == null || outputFilePath == null) {
                throw new IllegalArgumentException("Missing required parameters.");
            }

            CSVParser csvParser = new CSVParser(dataPath);
            try (BufferedReader reader = csvParser.getBufferedReader()) {
                CSVSearch csvSearch = new CSVSearch(reader, indexedColumnId);

                List<String> searchStrings = readInputFile(inputFilePath);

                Map<String, SearchResult> searchResults = new HashMap<>();
                long initTime = System.currentTimeMillis();
                for (String searchString : searchStrings) {
                    long startTime = System.currentTimeMillis();
                    List<Integer> resultIndices = csvSearch.search(searchString);
                    List<String> resultValues = getResultValues(csvParser, resultIndices);
                    long endTime = System.currentTimeMillis();
                    long timeElapsed = endTime - startTime;
                    searchResults.put(searchString, new SearchResult(searchString, resultValues, timeElapsed));
                }
                long totalTime = System.currentTimeMillis() - initTime;

                writeOutputJson(searchResults, totalTime, outputFilePath);
            } catch (IOException e) {
                System.err.println("Error reading CSV file: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static List<String> readInputFile(String inputFilePath) {
        List<String> searchStrings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                searchStrings.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return searchStrings;
    }

    private static List<String> getResultValues(CSVParser csvParser, List<Integer> resultIndices) throws IOException {
        List<String> resultValues = new ArrayList<>();
        try (BufferedReader reader = csvParser.getBufferedReader()) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                if (resultIndices.contains(index)) {
                    String[] columns = line.split(",");
                    resultValues.add(columns[0]);
                }
                index++;
            }
        }
        return resultValues;
    }

    private static void writeOutputJson(Map<String, SearchResult> searchResults, long initTime, String outputFilePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            OutputJson outputJson = new OutputJson(initTime, searchResults.values());
            mapper.writeValue(new File(outputFilePath), outputJson);
        } catch (IOException e) {
            System.err.println("Error writing JSON output: " + e.getMessage());
        }
    }
}

