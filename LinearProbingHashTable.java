class LinearProbingHashTable {
    private String[] keys;
    private Integer[] values;
    private final int M;
    private final HashFunctionType hashFunctionType;

    public LinearProbingHashTable(int M, HashFunctionType hashFunctionType) {
        this.M = M;
        this.hashFunctionType = hashFunctionType;
        keys = new String[M];
        values = new Integer[M];
    }

    // Insert a key-value pair
    public void put(String key, int value) {
        int index = hash(key);
        while (keys[index] != null) {
            if (keys[index].equals(key)) {
                values[index] = value; // Update existing key
                return;
            }
            index = (index + 1) % M; // Linear probing
        }
        keys[index] = key;
        values[index] = value;
    }

    // Check if a key exists
    public boolean contains(String key) {
        return get(key) != null;
    }

    // Get the value associated with a key
    public Integer get(String key) {
        int index = hash(key);
        while (keys[index] != null) {
            if (keys[index].equals(key)) {
                return values[index];
            }
            index = (index + 1) % M; // Linear probing
        }
        return null;
    }

    // Get the search cost (number of comparisons)
    public int getSearchCost(String key) {
        int index = hash(key);
        int comparisons = 0;
        while (keys[index] != null) {
            comparisons++;
            if (keys[index].equals(key)) {
                return comparisons;
            }
            index = (index + 1) % M; // Linear probing
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
