import java.io.*;
import java.util.*;

public class Main {
    private static final int SEPARATE_CHAINING_SIZE = 1000;
    private static final int LINEAR_PROBING_SIZE = 20000;

    public static void main(String[] args) throws IOException {
        // Load dictionary into hash tables
        String filePath = "C:\\Users\\wolfg\\IdeaProjects\\Assignment4\\src\\MITwordlist.txt"; // Adjust path as needed
        List<String> dictionary = loadDictionary(filePath);

        // Hash tables
        SeparateChainingHashTable separateOld = new SeparateChainingHashTable(SEPARATE_CHAINING_SIZE, HashFunctionType.OLD);
        SeparateChainingHashTable separateNew = new SeparateChainingHashTable(SEPARATE_CHAINING_SIZE, HashFunctionType.NEW);
        LinearProbingHashTable linearOld = new LinearProbingHashTable(LINEAR_PROBING_SIZE, HashFunctionType.OLD);
        LinearProbingHashTable linearNew = new LinearProbingHashTable(LINEAR_PROBING_SIZE, HashFunctionType.NEW);

        // Insert dictionary words
        for (int i = 0; i < dictionary.size(); i++) {
            String word = dictionary.get(i);
            int lineNumber = i + 1;
            separateOld.put(word, lineNumber);
            separateNew.put(word, lineNumber);
            linearOld.put(word, lineNumber);
            linearNew.put(word, lineNumber);
        }

        // Test passwords
        String[] passwords = {"account8", "accountability", "9a$D#qW7!uX&Lv3zT", "B@k45*W!c$Y7#zR9P", "X$8vQ!mW#3Dz&Yr4K5"};
        for (String password : passwords) {
            System.out.println("\nChecking password: " + password);
            checkPassword(password, separateOld, separateNew, linearOld, linearNew);
        }
    }

    private static List<String> loadDictionary(String filePath) throws IOException {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line.trim());
            }
        }
        return words;
    }

    private static void checkPassword(String password, SeparateChainingHashTable separateOld, SeparateChainingHashTable separateNew,
                                      LinearProbingHashTable linearOld, LinearProbingHashTable linearNew) {
        // Check strong password criteria
        boolean isStrong = true;
        int comparisonsOldSeparate = 0, comparisonsNewSeparate = 0;
        int comparisonsOldLinear = 0, comparisonsNewLinear = 0;

        if (password.length() < 8) {
            isStrong = false;
            System.out.println("Password is too short.");
        }

        comparisonsOldSeparate += separateOld.getSearchCost(password);
        comparisonsNewSeparate += separateNew.getSearchCost(password);
        comparisonsOldLinear += linearOld.getSearchCost(password);
        comparisonsNewLinear += linearNew.getSearchCost(password);

        if (separateOld.contains(password) || separateNew.contains(password) || linearOld.contains(password) || linearNew.contains(password)) {
            isStrong = false;
            System.out.println("Password is a dictionary word.");
        }

        // Check dictionary word followed by a digit
        if (password.length() > 1 && Character.isDigit(password.charAt(password.length() - 1))) {
            String wordPart = password.substring(0, password.length() - 1); // All except the last character
            char digitPart = password.charAt(password.length() - 1);       // The last character

            if (Character.isDigit(digitPart)) {
                // Update comparisons for search cost
                comparisonsOldSeparate += separateOld.getSearchCost(wordPart);
                comparisonsNewSeparate += separateNew.getSearchCost(wordPart);
                comparisonsOldLinear += linearOld.getSearchCost(wordPart);
                comparisonsNewLinear += linearNew.getSearchCost(wordPart);

                // Check if the word part exists in any dictionary
                if (separateOld.contains(wordPart) || separateNew.contains(wordPart) || linearOld.contains(wordPart) || linearNew.contains(wordPart)) {
                    isStrong = false;
                    System.out.println("Password is a dictionary word followed by a digit.");
                }
            }
        }


        System.out.println("Password is " + (isStrong ? "strong." : "weak."));
        System.out.println("Comparisons for separate chaining (old): " + comparisonsOldSeparate);
        System.out.println("Comparisons for separate chaining (new): " + comparisonsNewSeparate);
        System.out.println("Comparisons for linear probing (old): " + comparisonsOldLinear);
        System.out.println("Comparisons for linear probing (new): " + comparisonsNewLinear);
    }
}

// Enums for hash function types
enum HashFunctionType { OLD, NEW }