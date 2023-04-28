package edu.gcc.comp350.team4project;

import java.util.*;

public class SearchSuggestions {
    WordTree tree;
    String query;
    ArrayList<Course> courses;
    Scanner input; //use for now, update later
    public SearchSuggestions(ArrayList<Course> courses) {
        this.courses = courses;
        initTree();
        getQuery();
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

    public List<String> getSuggestions() {
        List<String> suggestions = new ArrayList<>();
        TrieNode currNode = tree.getRoot();

        //move currNode to the lowest position of query
        //i.e.: if query is soft, move currNode from s down to t assuming that is in the trie
        for (int i = 0; i < query.length(); i++) {
            char val = query.toUpperCase().charAt(i);
            int index = getIndex(val);
            if (currNode.getChildren()[index] != null) currNode = currNode.getChildren()[index];
            else return suggestions; //no suggestions found
        }

        Queue<String> queue = new LinkedList<>();

        //dfs from currNode down
        dfs(currNode, query.toUpperCase(), queue);

        //add each element from the queue to suggestions
        while (!queue.isEmpty()) {
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
            default -> val - 'A';
        };
    }
}
