package edu.gcc.comp350.team4project;

import java.util.*;

public class SearchSuggestions {
    private WordTree tree;
    private String query;
    private ArrayList<Course> courses;
    private Scanner input;
    public SearchSuggestions(ArrayList<Course> courses) {
        this.courses = courses;
        System.out.println(courses.size());
        initTree();
    }

    public void getQuery() {
        input = new Scanner(System.in);
        System.out.println("Please enter a search query: ");
        this.query = input.nextLine();
    }

    private void initTree() {
        tree = new WordTree();
        for (Course c: courses) tree.addCourse(c);
    }

    public List<String> getSuggestions(String query) {
        System.out.println("getSuggestions called with query: " + query);
        this.query = query;
        List<String> suggestions = new ArrayList<>();
        TrieNode currNode = tree.getRoot();
        Queue<String> queue = new LinkedList<>();

        //move currNode to the lowest position of query
        //i.e.: if the query is "soft", move currNode from s down to t assuming that is in the trie
        for (int i = 0; i < query.length(); i++) {
            char val = query.toUpperCase().charAt(i);
            int index = getIndex(val);
            if (currNode.getChildren()[index] != null) currNode = currNode.getChildren()[index];
            else return suggestions; //no suggestions found
        }

        dfs(currNode, query.toUpperCase(), queue); //dfs from currNode down

        while (!queue.isEmpty()) { //add each element form the queue to suggestions
            String suggestion = queue.poll();
            suggestions.add(suggestion);
        }
        return suggestions;
    }

    private void dfs(TrieNode currNode, String currentString, Queue<String> queue) {
        if (currNode == null) return; //base case

        if (currNode.getEndOfWord()) { //if node signals end of course name, add to queue
            queue.offer(currentString);
            if (queue.size() > 5) queue.poll(); //keep queue to size 5
        }

        //recursive traversal through children of currNode
        for (int i = 0; i < currNode.getChildren().length; i++) {
            TrieNode childNode = currNode.getChildren()[i];
            if (childNode != null) dfs(childNode, currentString + childNode.getValue(), queue);
        }
    }

    private int getIndex(char val) {
        System.out.println(val);
        return switch (val) {
            case ' ' -> 26;
            case '/' -> 27;
            case '&' -> 28;
            case ':' -> 29;
            case '-' -> 30;
            case '\'' -> 31;
            case '.' -> 32;
            case '1' -> 33;
            case '2' -> 34;
            case '3' -> 35;
            case '4' -> 36;
            case '5' -> 37;
            case '6' -> 38;
            case '7' -> 39;
            case '8' -> 40;
            case '9' -> 41;
            case '0' -> 42;
            case '(' -> 43;
            case ')' -> 44;
            default -> val - 'A';
        };
    }
}
