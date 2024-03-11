/*
 * Author: Dominic Jefferson and Joseph Loreen
 * Date: 3/09/2024
 * Purpose: Implement Huffman Coding
 */
package lab7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

class Node implements Comparable<Node> {
    char character;
    int frequency;
    Node left, right;

    public Node(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        left = right = null;
    }

    @Override
    public int compareTo(Node other) {
        return this.frequency - other.frequency;
    }
}

public class Huffman {

    // Counts frequencies of characters in the input string
    private static HashMap<Character, Integer> countFrequencies(String input) {
        HashMap<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : input.toCharArray()) { // Increment the frequency count for the current char in the frequency map
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1); // Sets default frequency to 0 and add 1 if found
        }
        return frequencyMap;
    }

    // Builds tree based on character frequencies
    private static Node buildTree(HashMap<Character, Integer> frequencyMap) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (char c : frequencyMap.keySet()) {
            pq.add(new Node(c, frequencyMap.get(c)));
        }
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node merged = new Node('\0', left.frequency + right.frequency); // Create new node with frequency equal to sum
            merged.left = left; // Make child of new node
            merged.right = right;// Make child of new node
            pq.add(merged); // Add new node back to priority queue
        }
        return pq.poll(); // Return root of Huffman tree
    }

    // Builds encoding table
    private static HashMap<Character, String> encodingTable(Node root) {
        HashMap<Character, String> encodingTable = new HashMap<>();
        encodingTable(root, "", encodingTable);
        return encodingTable;
    }

    // Recursive helper to build encoding table
    private static void encodingTable(Node node, String code, HashMap<Character, String> encodingTable) {
        if (node != null) { 
            if (node.left == null && node.right == null) { //If leaf add charcter and code to encoding table
                encodingTable.put(node.character, code);
            }
            encodingTable(node.left, code + "0", encodingTable); // Traverse left child with code 0 appended to current node
            encodingTable(node.right, code + "1", encodingTable);// Traverse right chuld with code 1 appended to current node
        }
    }

    // Encodes input string using encoding table
    private static String encode(String input, HashMap<Character, String> encodingTable) {
        StringBuilder encoded = new StringBuilder();
        for (char c : input.toCharArray()) { // Append code for each character in input to encoded string
            encoded.append(encodingTable.get(c));
        }
        return encoded.toString();
    }

    // Decodes encoded string using Huffman tree
    private static String decode(String encoded, Node root) {
        StringBuilder decoded = new StringBuilder();
        Node current = root;
        for (char bit : encoded.toCharArray()) {
            if (bit == '0') { // Traverse based on the bits in the encoded string
                current = current.left;
            } else {
                current = current.right;
            }
            if (current.left == null && current.right == null) { // if leaf node reached, append its char to decoded string, reset current node to root
                decoded.append(current.character);
                current = root;
            }
        }
        return decoded.toString();
    }

    public static void main(String[] args) {
        // Read input from file
        String filename = args[0];
        StringBuilder inputBuilder = new StringBuilder();
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) { // Read each line and appaned to inputBuilder
                inputBuilder.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            return;
        }
        String input = inputBuilder.toString().trim();

        HashMap<Character, Integer> frequencyMap = countFrequencies(input); // Count frequencies of char in input string
        Node root = buildTree(frequencyMap); // Build Huffman tree based on char frequencies
        HashMap<Character, String> encodingTable = encodingTable(root); // Build encoding table

        String encoded = encode(input, encodingTable);
        String decoded = decode(encoded, root);

        if (input.length() < 100) {
            System.out.println("Input string: " + input);
            System.out.println("Encoded string: " + encoded);
            System.out.println("Decoded string: " + decoded);
        }

        System.out.println("Decoded equals input: " + input.equals(decoded)); // Checks if decoded string equals input string
        double compressionRatio = (double) encoded.length() / (input.length() * 8); // Calculate compression ratio
        System.out.println("Compression ratio: " + compressionRatio);
    }
}
