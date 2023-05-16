package edu.gcc.comp350.team4project;

public class WordTree {
    private TrieNode root;
    public WordTree() { root = new TrieNode(' '); }

    public void addCourse(ScheduleElement c) {
        String courseName = c.getName().toUpperCase(); //get the name of the course, we work with only uppercase letters
        TrieNode ptr = root; //set a pointer to the root of the tree
        for (int i = 0; i < courseName.length(); i++) { //loop over the course name
            char val = courseName.charAt(i);
            int index = getIndex(val); //get the index of the letter in the course
            if (ptr.getChildren()[index] == null) {
                TrieNode s = new TrieNode(val);
                ptr.getChildren()[index] = s;
                ptr = s;
            }
            else ptr = ptr.getChildren()[index];
        }
        ptr.setEndOfWord(true); //after adding the whole course name to the tree, set the last character as the end of a course
    }

    public boolean containsCourse(ScheduleElement c) { //for testing, basically checks if a course name is in the tree
        String elementName = c.getName().toUpperCase();
        TrieNode ptr = root;
        for (int i = 0; i < elementName.length(); i++) {
            char val = elementName.charAt(i);
            int index = getIndex(val);
            if (ptr.getChildren()[index] != null)
                ptr = ptr.getChildren()[index];
            else return false;
        }
        return true;
    }

    public void printContents(String subWord, TrieNode c) { //for testing, prints every course name in the tree
        if (c.getEndOfWord()) System.out.println(subWord);

        for (int i = 0; i < c.getChildren().length; i++) {
            TrieNode node = c.getChildren()[i];
            if (node != null) printContents(subWord + node.getValue(), node);
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
            case '(' -> 43;
            case ')' -> 44;
            default -> val - 'A';
        };
    }

    public TrieNode getRoot() { return root; }
}
