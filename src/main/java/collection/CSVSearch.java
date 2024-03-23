package collection;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVSearch {
    private TrieNode root;
    private int indexedColumnId;

    static class TrieNode {
        Map<Character, TrieNode> children;
        List<Integer> indices;

        TrieNode() {
            children = new HashMap<>();
            indices = new ArrayList<>();
        }
    }

    public CSVSearch(BufferedReader reader, int indexedColumnId) {
        root = new TrieNode();
        this.indexedColumnId = indexedColumnId;
        buildTrie(reader);
    }

    private void insert(String key, int index) {
        TrieNode current = root;
        for (char c : key.toCharArray()) {
            current.children.putIfAbsent(Character.toLowerCase(c), new TrieNode());
            current = current.children.get(Character.toLowerCase(c));
            current.indices.add(index);
        }
    }

    public List<Integer> search(String prefix) {
        TrieNode current = root;
        for (char c : prefix.toLowerCase().toCharArray()) {
            if (!current.children.containsKey(c)) {
                return new ArrayList<>();
            }
            current = current.children.get(c);
        }
        return current.indices;
    }

    private void buildTrie(BufferedReader reader) {
        int index = 0;
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length > indexedColumnId) {
                    String searchString = columns[indexedColumnId].replaceAll("\"", "").trim().toLowerCase();
                    insert(searchString, index);
                }
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
