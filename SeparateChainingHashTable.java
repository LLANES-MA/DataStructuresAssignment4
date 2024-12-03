import java.util.LinkedList;

class SeparateChainingHashTable {
    private LinkedList<Node>[] table;
    private final int M;
    private final HashFunctionType hashFunctionType;

    // Node class for storing key-value pairs
    private static class Node {
        String key;
        int value;

        Node(String key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    public SeparateChainingHashTable(int M, HashFunctionType hashFunctionType) {
        this.M = M;
        this.hashFunctionType = hashFunctionType;
        table = (LinkedList<Node>[]) new LinkedList[M];
        for (int i = 0; i < M; i++) {
            table[i] = new LinkedList<>();
        }
    }

    // Insert a key-value pair
    public void put(String key, int value) {
        int index = hash(key);
        for (Node node : table[index]) {
            if (node.key.equals(key)) {
                node.value = value; // Update existing key
                return;
            }
        }
        table[index].add(new Node(key, value)); // Insert new key-value pair
    }

    // Check if a key exists
    public boolean contains(String key) {
        int index = hash(key);
        for (Node node : table[index]) {
            if (node.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    // Get the value associated with a key
    public Integer get(String key) {
        int index = hash(key);
        for (Node node : table[index]) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    // Get the search cost (number of comparisons)
    public int getSearchCost(String key) {
        int index = hash(key);
        int comparisons = 0;
        for (Node node : table[index]) {
            comparisons++;
            if (node.key.equals(key)) {
                return comparisons;
            }
        }
        return comparisons; // Return even if not found
    }

    // Hash function implementation
    private int hash(String key) {
        int hash = 0;
        if (hashFunctionType == HashFunctionType.OLD) {
            int skip = Math.max(1, key.length() / 8);
            for (int i = 0; i < key.length(); i += skip) {
                hash = (hash * 37) + key.charAt(i);
            }
        } else { // NEW
            for (int i = 0; i < key.length(); i++) {
                hash = (hash * 31) + key.charAt(i);
            }
        }
        return (hash & 0x7fffffff) % M;
    }
}

